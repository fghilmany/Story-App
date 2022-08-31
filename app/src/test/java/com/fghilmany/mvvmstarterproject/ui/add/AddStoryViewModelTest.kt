package com.fghilmany.mvvmstarterproject.ui.add

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.fghilmany.mvvmstarterproject.DataDummy
import com.fghilmany.mvvmstarterproject.core.data.DataRepository
import com.fghilmany.mvvmstarterproject.core.data.Resource
import com.fghilmany.mvvmstarterproject.core.data.remote.response.BasicResponse
import com.fghilmany.mvvmstarterproject.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var dataRepository: DataRepository

    private lateinit var addStoryViewModel: AddStoryViewModel
    private val dataDummy = DataDummy.basicResponse()
    private val dummyFile = DataDummy.multipartFile()
    private val dummyDescription = DataDummy.requestBody("Dummy Desctiprion")
    private val dummyLat = DataDummy.requestBody("-7.7856193")
    private val dummyLon = DataDummy.requestBody("110.46422")


    @Before
    fun setUp() {
        addStoryViewModel = AddStoryViewModel(dataRepository)
        addStoryViewModel.setStoryParam(dummyFile, dummyDescription, dummyLat, dummyLon)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when Add New Story Success`()  = runTest {
        val expectedResponse : Flow<Resource<BasicResponse>> = flow {
            emit(Resource.Success(dataDummy))
        }
        Mockito.`when`(dataRepository.addNewStory(dummyFile, dummyDescription, dummyLat, dummyLon)).thenReturn(
            expectedResponse
        )
        addStoryViewModel.postStory().observeForever {
            Mockito.verify(dataRepository).addNewStory(dummyFile, dummyDescription, dummyLat, dummyLon)
            assertNotNull(it.data)
            assertTrue(it is Resource.Success)
            assertFalse(it is Resource.Error)
            assertFalse(it.data?.error?:false)
        }
    }

    @Test
    fun `when Add New Story Failed`() = runTest {
        val expectedResponse : Flow<Resource<BasicResponse>> = flow {
            emit(Resource.Error("", null))
        }
        Mockito.`when`(dataRepository.addNewStory(dummyFile, dummyDescription, dummyLat, dummyLon)).thenReturn(
            expectedResponse
        )
        addStoryViewModel.postStory().observeForever {
            Mockito.verify(dataRepository).addNewStory(dummyFile, dummyDescription, dummyLat, dummyLon)
            assertNull(it.data)
            assertFalse(it is Resource.Success)
            assertTrue(it is Resource.Error)
            assertNotNull(it.message)
        }
    }
}