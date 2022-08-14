package com.fghilmany.mvvmstarterproject.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fghilmany.mvvmstarterproject.core.data.DataRepository

class HomeViewModel(private val dataRepository: DataRepository) : ViewModel() {

    fun getStories() = dataRepository.getStories("1", "20", null).asLiveData()

}