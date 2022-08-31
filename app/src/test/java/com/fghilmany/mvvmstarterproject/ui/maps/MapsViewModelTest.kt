package com.fghilmany.mvvmstarterproject.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.fghilmany.mvvmstarterproject.DataDummy
import com.fghilmany.mvvmstarterproject.core.data.DataRepository
import com.fghilmany.mvvmstarterproject.core.data.local.entity.StoryEntity
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
class MapsViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var dataRepository: DataRepository

    private lateinit var mapsViewModel: MapsViewModel
    private val dataDummy = DataDummy.storyFromDb()

    @Before
    fun setUp() {
        mapsViewModel = MapsViewModel(dataRepository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when get Story from local Success`()  = runTest {
        val expectedResponse : Flow<List<StoryEntity>> = flow {
            emit(dataDummy)
        }
        Mockito.`when`(dataRepository.getStoriesFromDb()).thenReturn(
            expectedResponse
        )
        mapsViewModel.getStories().observeForever {
            Mockito.verify(dataRepository).getStoriesFromDb()
            assertNotNull(it)
            assertTrue(it.isNotEmpty())
            assertEquals(it.size, dataDummy.size)
        }
    }

    @Test
    fun `when get Story from local Failed`() = runTest {
        val expectedResponse : Flow<List<StoryEntity>> = flow {
            emit(listOf())
        }
        Mockito.`when`(dataRepository.getStoriesFromDb()).thenReturn(
            expectedResponse
        )
        mapsViewModel.getStories().observeForever {
            Mockito.verify(dataRepository).getStoriesFromDb()
            assertNotNull(it)
            assertTrue(it.isEmpty())
        }
    }

}