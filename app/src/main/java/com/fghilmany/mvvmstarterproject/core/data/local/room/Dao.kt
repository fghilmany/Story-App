package com.fghilmany.mvvmstarterproject.core.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fghilmany.mvvmstarterproject.core.data.local.entity.RemoteKeys
import com.fghilmany.mvvmstarterproject.core.data.local.entity.StoryEntity

@Dao
interface Dao {
    @Query("SELECT * FROM story_table")
    fun getAllStories(): PagingSource<Int, StoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(storyEntity: List<StoryEntity>?)

    @Query("DELETE FROM story_table")
    fun deleteStory()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun getRemoteKeysId(id: String): RemoteKeys?

    @Query("DELETE FROM remote_keys")
    suspend fun deleteRemoteKeys()
}