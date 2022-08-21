package com.fghilmany.mvvmstarterproject.core.data.remote

import android.content.Context
import com.fghilmany.mvvmstarterproject.R
import com.fghilmany.mvvmstarterproject.core.data.remote.network.ApiResponse
import com.fghilmany.mvvmstarterproject.core.data.remote.network.ApiServices
import com.fghilmany.mvvmstarterproject.core.data.remote.response.BasicResponse
import com.fghilmany.mvvmstarterproject.core.data.remote.response.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber
import java.net.SocketTimeoutException

//use `context` if you want to use resource.getString() for call a message error or whatever
class RemoteDatasource(
    private val apiServices: ApiServices?,
    private val context: Context
) {

    suspend fun doLogin(email: String, password: String): Flow<ApiResponse<LoginResponse>> {
        return flow {
            try {
                val response = apiServices?.doLogin(email, password)
                if (response != null) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(exceptionLog(e, "doLogin"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun doRegister(
        email: String,
        password: String,
        name: String
    ): Flow<ApiResponse<BasicResponse>> {
        return flow {
            try {
                val response = apiServices?.doRegister(email, password, name)
                if (response != null) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(exceptionLog(e, "doRegister"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun addNewStory(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): Flow<ApiResponse<BasicResponse>> {
        return flow {
            try {
                val response = apiServices?.addNewStory(file, description, lat, lon)
                if (response != null) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(exceptionLog(e, "addNewStory"))
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun exceptionLog(e: Exception, tagLog: String): ApiResponse.Error {
        when (e) {
            is SocketTimeoutException -> {
                Timber.tag(tagLog).e(e.message.toString())
                return ApiResponse.Error(
                    e.message.toString() + ", " + context.resources.getString(
                        R.string.check_your_internet_connection
                    )
                )
            }

            is HttpException -> {
                return try {
                    val `object` = JSONObject(e.response()?.errorBody()?.string().toString())
                    val messageString: String = `object`.getString("message")
                    Timber.tag(tagLog).e(messageString)
                    ApiResponse.Error(messageString)
                } catch (e: Exception) {
                    val messageString = context.resources.getString(R.string.something_wrong)
                    Timber.tag(tagLog).e(messageString)
                    ApiResponse.Error(messageString)
                }
            }

            is NoSuchElementException -> {
                Timber.tag(tagLog).e(e.message.toString())
                return (ApiResponse.Error(
                    e.message.toString() + ", " + context.resources.getString(
                        R.string.check_your_internet_connection
                    )
                ))
            }

            else -> {
                val messageString = context.resources.getString(R.string.something_wrong)
                Timber.tag(tagLog).e(e)
                return ApiResponse.Error(messageString)
            }
        }
    }

}