package com.fghilmany.mvvmstarterproject.core.data

import androidx.paging.PagingData
import com.fghilmany.mvvmstarterproject.core.data.local.entity.StoryEntity
import com.fghilmany.mvvmstarterproject.core.data.paging.PagingDataSource
import com.fghilmany.mvvmstarterproject.core.data.remote.RemoteDatasource
import com.fghilmany.mvvmstarterproject.core.data.remote.network.ApiResponse
import com.fghilmany.mvvmstarterproject.core.data.remote.response.BasicResponse
import com.fghilmany.mvvmstarterproject.core.data.remote.response.LoginResponse
import com.fghilmany.mvvmstarterproject.core.utils.PreferenceProvider
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class DataRepository(
    private val remoteDatasource: RemoteDatasource,
    private val pagingDataSource: PagingDataSource,
    private val preferenceProvider: PreferenceProvider
) : IDataRepository {
    override fun doLogin(email: String, password: String): Flow<Resource<LoginResponse>> {
        return object : OnlineBoundResource<LoginResponse>() {
            override suspend fun createCall(): Flow<ApiResponse<LoginResponse>> {
                return remoteDatasource.doLogin(email, password)
            }

            override fun getResponse(data: LoginResponse) {
                preferenceProvider.setToken(data.loginResult?.token)
                preferenceProvider.setUserId(data.loginResult?.userId)
                preferenceProvider.setName(data.loginResult?.name)
            }
        }.asFlow()
    }

    override fun doRegister(
        email: String,
        password: String,
        name: String
    ): Flow<Resource<BasicResponse>> {
        return object : OnlineBoundResource<BasicResponse>() {
            override suspend fun createCall(): Flow<ApiResponse<BasicResponse>> {
                return remoteDatasource.doRegister(email, password, name)
            }

            override fun getResponse(data: BasicResponse) {

            }
        }.asFlow()
    }

    override fun getStories(): Flow<PagingData<StoryEntity>> {
        return pagingDataSource.getStories()
    }

    override fun addNewStory(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): Flow<Resource<BasicResponse>> {
        return object : OnlineBoundResource<BasicResponse>() {
            override suspend fun createCall(): Flow<ApiResponse<BasicResponse>> {
                return remoteDatasource.addNewStory(file, description, lat, lon)
            }

            override fun getResponse(data: BasicResponse) {

            }
        }.asFlow()
    }


}