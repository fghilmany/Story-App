package com.fghilmany.mvvmstarterproject.core.data.paging

import androidx.paging.*
import androidx.room.withTransaction
import com.fghilmany.mvvmstarterproject.core.data.local.entity.RemoteKeys
import com.fghilmany.mvvmstarterproject.core.data.local.entity.StoryEntity
import com.fghilmany.mvvmstarterproject.core.data.local.room.Database
import com.fghilmany.mvvmstarterproject.core.data.remote.network.ApiServices
import com.fghilmany.mvvmstarterproject.core.utils.INITIAL_PAGE_INDEX
import com.fghilmany.mvvmstarterproject.core.utils.LOCATION

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
        private val apiServices: ApiServices,
        private val database: Database
    ): RemoteMediator<Int, StoryEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH ->{
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val responseData = apiServices.getStories(page.toString(), state.config.pageSize.toString(), LOCATION).listStory

            val endOfPaginationReached = responseData?.isEmpty()?:true

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.dao().deleteRemoteKeys()
                    database.dao().deleteStory()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = responseData?.map {
                    RemoteKeys(id = it.id.toString(), prevKey = prevKey, nextKey = nextKey)
                }
                keys?.let { database.dao().insertAll(keys) }
                val storyMap = responseData?.map {
                    with(it){
                        StoryEntity(
                            photoUrl, createdAt, name, description, lon, id.toString(), lat
                        )
                    }
                }
                database.dao().insertStory(storyMap)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.dao().getRemoteKeysId(data.id.toString())
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.dao().getRemoteKeysId(data.id.toString())
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.dao().getRemoteKeysId(id)
            }
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }


}