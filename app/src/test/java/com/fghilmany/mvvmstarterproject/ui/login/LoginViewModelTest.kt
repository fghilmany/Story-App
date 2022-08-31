package com.fghilmany.mvvmstarterproject.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.fghilmany.mvvmstarterproject.DataDummy
import com.fghilmany.mvvmstarterproject.core.data.DataRepository
import com.fghilmany.mvvmstarterproject.core.data.Resource
import com.fghilmany.mvvmstarterproject.core.data.remote.response.LoginResponse
import com.fghilmany.mvvmstarterproject.core.utils.PreferenceProvider
import com.fghilmany.mvvmstarterproject.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var dataRepository: DataRepository
    private lateinit var loginViewModel: LoginViewModel
    private val dataDummy = DataDummy.loginResponse()
    private val dummyEmail = "test123@test.co.id"
    private val dummyPassword = "12345678"

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(dataRepository)
        loginViewModel.setLoginParam(dummyEmail, dummyPassword)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when Login Success`()  = runTest {
        val expectedResponse : Flow<Resource<LoginResponse>> = flow {
            emit(Resource.Success(dataDummy))
        }
        `when`(dataRepository.doLogin(dummyEmail, dummyPassword)).thenReturn(
            expectedResponse
        )
        loginViewModel.doLogin().observeForever {
            verify(dataRepository).doLogin(dummyEmail, dummyPassword)
            Assert.assertNotNull(it.data)
            Assert.assertTrue(it is Resource.Success)
            Assert.assertFalse(it is Resource.Error)
            Assert.assertFalse(it.data?.error?:false)
        }
    }

    @Test
    fun `when Login Failed`() = runTest {
        val expectedResponse : Flow<Resource<LoginResponse>> = flow {
            emit(Resource.Error("", null))
        }
        `when`(dataRepository.doLogin(dummyEmail, dummyPassword)).thenReturn(
            expectedResponse
        )
        loginViewModel.doLogin().observeForever {
            verify(dataRepository).doLogin(dummyEmail, dummyPassword)
            Assert.assertNull(it.data)
            Assert.assertFalse(it is Resource.Success)
            Assert.assertTrue(it is Resource.Error)
            Assert.assertNotNull(it.message)
        }
    }
}