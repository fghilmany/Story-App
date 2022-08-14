package com.fghilmany.mvvmstarterproject.core.data

import com.fghilmany.mvvmstarterproject.core.data.remote.network.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import timber.log.Timber

abstract class OnlineBoundResource<RequestType> {
    private var result: Flow<Resource<RequestType>> = flow {

        emit(Resource.Loading())
        when (val apiResponse = createCall().first()) {
            is ApiResponse.Success -> {
                getResponse(apiResponse.body)
                emit(Resource.Success(apiResponse.body))
            }
            is ApiResponse.Empty -> {}
            is ApiResponse.Error -> {
                emit(Resource.Error(apiResponse.errorMessage))
                Timber.e(apiResponse.errorMessage)
            }
        }

    }

    protected abstract suspend fun createCall(): Flow<ApiResponse<RequestType>>

    protected abstract fun getResponse(data: RequestType)

    fun asFlow(): Flow<Resource<RequestType>> = result
}