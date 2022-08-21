package com.fghilmany.mvvmstarterproject.core.data.local

import androidx.paging.PagingSource
import com.fghilmany.mvvmstarterproject.core.data.local.entity.StoryEntity
import com.fghilmany.mvvmstarterproject.core.data.local.room.Dao

class LocalDatasource (
    private val dao: Dao
) {
    fun getStories(): PagingSource<Int, StoryEntity> = dao.getAllStories()
    suspend fun insertStories(storyEntity: List<StoryEntity>) {
        return dao.insertStory(storyEntity)
    }
    fun delete() = dao.deleteStory()
}