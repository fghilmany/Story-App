package com.fghilmany.mvvmstarterproject.ui.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fghilmany.mvvmstarterproject.core.data.DataRepository

class MapsViewModel(private val dataRepository: DataRepository): ViewModel() {

    fun getStories() = dataRepository.getStoriesFromDb().asLiveData()
}