package com.fghilmany.mvvmstarterproject.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.fghilmany.mvvmstarterproject.DataDummy
import com.fghilmany.mvvmstarterproject.core.data.DataRepository
import com.fghilmany.mvvmstarterproject.core.data.local.entity.StoryEntity
import com.fghilmany.mvvmstarterproject.utils.MainDispatcherRule
import com.fghilmany.mvvmstarterproject.utils.PagedTestDataSource.Companion.snapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var dataRepository: DataRepository

    private lateinit var homeViewModel: HomeViewModel
    private val dataDummy = DataDummy.storyFromDb()

    @Before
    fun setUp() {
        homeViewModel = HomeViewModel(dataRepository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when get Story Success`()  = runTest {
        val data = snapshot(dataDummy)
        val expectedResponse : Flow<PagingData<StoryEntity>> = flow {
            emit(data)
        }

        Mockito.`when`(dataRepository.getStories()).thenReturn(
            expectedResponse
        )
        homeViewModel.getStories().observeForever {
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

            Mockito.verify(dataRepository).getStories()
            assertNotNull(differ.snapshot())
            assertEquals(differ.snapshot().size, dataDummy.size)
        }
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}