package com.fghilmany.mvvmstarterproject.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fghilmany.mvvmstarterproject.core.data.DataRepository

class LoginViewModel(private val dataRepository: DataRepository) : ViewModel() {

    var email = ""
    var password = ""

    fun setLoginParam(email: String, password: String) {
        this.email = email
        this.password = password
    }

    fun doLogin() = dataRepository.doLogin(email, password).asLiveData()
}