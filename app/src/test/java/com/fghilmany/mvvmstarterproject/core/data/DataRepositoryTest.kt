package com.fghilmany.mvvmstarterproject.core.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.fghilmany.mvvmstarterproject.DataDummy
import com.fghilmany.mvvmstarterproject.core.data.local.LocalDatasource
import com.fghilmany.mvvmstarterproject.core.data.local.entity.StoryEntity
import com.fghilmany.mvvmstarterproject.core.data.paging.PagingDataSource
import com.fghilmany.mvvmstarterproject.core.data.remote.RemoteDatasource
import com.fghilmany.mvvmstarterproject.core.data.remote.network.ApiResponse
import com.fghilmany.mvvmstarterproject.core.data.remote.response.BasicResponse
import com.fghilmany.mvvmstarterproject.core.data.remote.response.LoginResponse
import com.fghilmany.mvvmstarterproject.core.utils.PreferenceProvider
import com.fghilmany.mvvmstarterproject.ui.home.HomeAdapter
import com.fghilmany.mvvmstarterproject.utils.MainDispatcherRule
import com.fghilmany.mvvmstarterproject.utils.PagedTestDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DataRepositoryTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var remoteDatasource: RemoteDatasource
    @Mock
    private lateinit var localDatasource: LocalDatasource
    @Mock
    private lateinit var pagingDataSource: PagingDataSource
    @Mock
    private lateinit var preferenceProvider: PreferenceProvider
    private lateinit var dataRepository: DataRepository
    private val dummyEmail = "test123@test.co.id"
    private val dummyPassword = "12345678"
    private val dummyName = "faris"
    private val dummyLogin = DataDummy.loginResponse()
    private val dummyBasicResponse = DataDummy.basicResponse()
    private val dummyStoryEntity = DataDummy.storyFromDb()
    private val dummyFile = DataDummy.multipartFile()
    private val dummyDescription = DataDummy.requestBody("Dummy Desctiprion")
    private val dummyLat = DataDummy.requestBody("-7.7856193")
    private val dummyLon = DataDummy.requestBody("110.46422")


    @Before
    fun setup(){
        dataRepository = DataRepository(remoteDatasource, localDatasource, pagingDataSource, preferenceProvider)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when Login Success`()  = runTest {
        val expectedResponse : Flow<ApiResponse<LoginResponse>> = flow {
            emit(ApiResponse.Success(dummyLogin))
        }
        `when`(remoteDatasource.doLogin(dummyEmail, dummyPassword)).thenReturn(
            expectedResponse
        )
        dataRepository.doLogin(dummyEmail, dummyPassword).collect {
            if (it !is Resource.Loading){
                verify(remoteDatasource).doLogin(dummyEmail, dummyPassword)
                assertNotNull(it.data)
                assertTrue(it is Resource.Success)
                assertFalse(it is Resource.Error)
                assertFalse(it.data?.error?:false)
            }
        }
    }

    @Test
    fun `when Login Failed`() = runTest {
        val expectedResponse : Flow<ApiResponse<LoginResponse>> = flow {
            emit(ApiResponse.Error(""))
        }
        `when`(remoteDatasource.doLogin(dummyEmail, dummyPassword)).thenReturn(
            expectedResponse
        )

        dataRepository.doLogin(dummyEmail, dummyPassword).collect {
            if (it !is Resource.Loading){
                verify(remoteDatasource).doLogin(dummyEmail, dummyPassword)
                assertNull(it.data)
                assertFalse(it is Resource.Success)
                assertTrue(it is Resource.Error)
                assertNotNull(it.message)
            }
        }
    }

    @Test
    fun `when Register Success`()  = runTest {
        val expectedResponse : Flow<ApiResponse<BasicResponse>> = flow {
            emit(ApiResponse.Success(dummyBasicResponse))
        }
        `when`(remoteDatasource.doRegister(dummyEmail, dummyPassword, dummyName)).thenReturn(
            expectedResponse
        )
        dataRepository.doRegister(dummyEmail, dummyPassword, dummyName).collect {
            if (it !is Resource.Loading){
                verify(remoteDatasource).doRegister(dummyEmail, dummyPassword, dummyName)
                assertNotNull(it.data)
                assertTrue(it is Resource.Success)
                assertFalse(it is Resource.Error)
                assertFalse(it.data?.error?:false)
            }
        }
    }

    @Test
    fun `when Register Failed`() = runTest {
        val expectedResponse : Flow<ApiResponse<BasicResponse>> = flow {
            emit(ApiResponse.Error("error"))
        }
        `when`(remoteDatasource.doRegister(dummyEmail, dummyPassword, dummyName)).thenReturn(
            expectedResponse
        )
        dataRepository.doRegister(dummyEmail, dummyPassword, dummyName).collect {
            if (it !is Resource.Loading){
                verify(remoteDatasource).doRegister(dummyEmail, dummyPassword, dummyName)
                assertNull(it.data)
                assertFalse(it is Resource.Success)
                assertTrue(it is Resource.Error)
                assertNotNull(it.message)
            }
        }
    }

    @Test
    fun `when get Story Success`()  = runTest {
        val data = PagedTestDataSource.snapshot(dummyStoryEntity)
        val expectedResponse : Flow<PagingData<StoryEntity>> = flow {
            emit(data)
        }

        `when`(pagingDataSource.getStories()).thenReturn(
            expectedResponse
        )
        dataRepository.getStories().collect {
            val differ = AsyncPagingDataDiffer(
                diffCallback = HomeAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                mainDispatcher = mainDispatcherRule.testDispatcher,
                workerDispatcher = mainDispatcherRule.testDispatcher
            )
            CoroutineScope(Dispatchers.IO).launch {
                differ.submitData(it)
            }
            advanceUntilIdle()

            verify(pagingDataSource).getStories()
            assertNotNull(differ.snapshot())
            assertEquals(differ.snapshot().size, dummyStoryEntity.size)
        }
    }

    @Test
    fun `when get Story from local Success`()  = runTest {
        val expectedResponse : Flow<List<StoryEntity>> = flow {
            emit(dummyStoryEntity)
        }
        `when`(localDatasource.getStories()).thenReturn(
            expectedResponse
        )
        dataRepository.getStoriesFromDb().collect {
            verify(localDatasource).getStories()
            assertNotNull(it)
            assertTrue(it.isNotEmpty())
            assertEquals(it.size, dummyStoryEntity.size)
        }
    }

    @Test
    fun `when Add New Story Success`()  = runTest {
        val expectedResponse : Flow<ApiResponse<BasicResponse>> = flow {
            emit(ApiResponse.Success(dummyBasicResponse))
        }
        `when`(remoteDatasource.addNewStory(dummyFile, dummyDescription, dummyLat, dummyLon)).thenReturn(
            expectedResponse
        )
        dataRepository.addNewStory(dummyFile, dummyDescription, dummyLat, dummyLon).collect {
            if (it !is Resource.Loading){
                verify(remoteDatasource).addNewStory(dummyFile, dummyDescription, dummyLat, dummyLon)
                assertNotNull(it.data)
                assertTrue(it is Resource.Success)
                assertFalse(it is Resource.Error)
                assertFalse(it.data?.error?:false)
            }
        }
    }

    @Test
    fun `when Add New Story Failed`() = runTest {
        val expectedResponse : Flow<ApiResponse<BasicResponse>> = flow {
            emit(ApiResponse.Error("error"))
        }
        `when`(remoteDatasource.addNewStory(dummyFile, dummyDescription, dummyLat, dummyLon)).thenReturn(
            expectedResponse
        )
        dataRepository.addNewStory(dummyFile, dummyDescription, dummyLat, dummyLon).collect {
            if (it !is Resource.Loading){
                verify(remoteDatasource).addNewStory(dummyFile, dummyDescription, dummyLat, dummyLon)
                assertNull(it.data)
                assertFalse(it is Resource.Success)
                assertTrue(it is Resource.Error)
                assertNotNull(it.message)
            }
        }
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}