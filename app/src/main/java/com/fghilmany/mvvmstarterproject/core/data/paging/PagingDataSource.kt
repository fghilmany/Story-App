package com.fghilmany.mvvmstarterproject.core.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.fghilmany.mvvmstarterproject.core.data.local.entity.StoryEntity
import com.fghilmany.mvvmstarterproject.core.data.local.room.Database
import com.fghilmany.mvvmstarterproject.core.data.remote.network.ApiServices
import kotlinx.coroutines.flow.Flow

class PagingDataSource(
    private val database: Database,
    private val apiServices: ApiServices,
    ) {

    fun getStories(): Flow<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator =
                StoryRemoteMediator(apiServices, database)
            ,
            pagingSourceFactory = {
                database.dao().getAllStories()
            }
        ).flow
    }

}