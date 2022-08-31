package com.fghilmany.mvvmstarterproject.ui.register

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
class RegisterViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var dataRepository: DataRepository

    private lateinit var registerViewModel: RegisterViewModel
    private val dataDummy = DataDummy.basicResponse()
    val dummyEmail = "test123@test.co.id"
    private val dummyPassword = "12345678"
    private val dummyName = "Faris"

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(dataRepository)
        registerViewModel.setRegisterParam(dummyName, dummyEmail, dummyPassword)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when Register Success`()  = runTest {
        val expectedResponse : Flow<Resource<BasicResponse>> = flow {
            emit(Resource.Success(dataDummy))
        }
        Mockito.`when`(dataRepository.doRegister(dummyEmail, dummyPassword, dummyName)).thenReturn(
            expectedResponse
        )
        registerViewModel.doRegister().observeForever {
            Mockito.verify(dataRepository).doRegister(dummyEmail, dummyPassword, dummyName)
            assertNotNull(it.data)
            assertTrue(it is Resource.Success)
            assertFalse(it is Resource.Error)
            assertFalse(it.data?.error?:false)
        }
    }

    @Test
    fun `when Register Failed`() = runTest {
        val expectedResponse : Flow<Resource<BasicResponse>> = flow {
            emit(Resource.Error("", null))
        }
        Mockito.`when`(dataRepository.doRegister(dummyEmail, dummyPassword, dummyName)).thenReturn(
            expectedResponse
        )
        registerViewModel.doRegister().observeForever {
            Mockito.verify(dataRepository).doRegister(dummyEmail, dummyPassword, dummyName)
            assertNull(it.data)
            assertFalse(it is Resource.Success)
            assertTrue(it is Resource.Error)
            assertNotNull(it.message)
        }
    }
}