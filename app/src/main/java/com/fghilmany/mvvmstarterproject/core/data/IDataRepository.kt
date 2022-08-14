package com.fghilmany.mvvmstarterproject.core.data

import com.fghilmany.mvvmstarterproject.core.data.remote.response.BasicResponse
import com.fghilmany.mvvmstarterproject.core.data.remote.response.LoginResponse
import com.fghilmany.mvvmstarterproject.core.data.remote.response.StoriesResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface IDataRepository {

    fun doLogin(email: String, password: String): Flow<Resource<LoginResponse>>

    fun doRegister(email: String, password: String, name: String): Flow<Resource<BasicResponse>>

    fun getStories(page: String?, size: String?, location: String?): Flow<Resource<StoriesResponse>>

    fun addNewStory(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): Flow<Resource<BasicResponse>>

}