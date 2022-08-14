package com.fghilmany.mvvmstarterproject.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fghilmany.mvvmstarterproject.core.data.DataRepository

class RegisterViewModel(private val dataRepository: DataRepository) : ViewModel() {

    private var name = ""
    private var email = ""
    private var password = ""

    fun setRegisterParam(name: String, email: String, password: String) {
        this.name = name
        this.email = email
        this.password = password
    }

    fun doRegister() = dataRepository.doRegister(email, password, name).asLiveData()

}