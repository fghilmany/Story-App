package com.fghilmany.mvvmstarterproject.core.data

import androidx.paging.PagingData
import com.fghilmany.mvvmstarterproject.core.data.local.entity.StoryEntity
import com.fghilmany.mvvmstarterproject.core.data.remote.response.BasicResponse
import com.fghilmany.mvvmstarterproject.core.data.remote.response.ListStoryItem
import com.fghilmany.mvvmstarterproject.core.data.remote.response.LoginResponse
import com.fghilmany.mvvmstarterproject.core.data.remote.response.StoriesResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface IDataRepository {

    fun doLogin(email: String, password: String): Flow<Resource<LoginResponse>>

    fun doRegister(email: String, password: String, name: String): Flow<Resource<BasicResponse>>

    fun getStories(): Flow<PagingData<StoryEntity>>

    fun addNewStory(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): Flow<Resource<BasicResponse>>

}