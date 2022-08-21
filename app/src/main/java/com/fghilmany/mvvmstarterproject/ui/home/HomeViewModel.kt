package com.fghilmany.mvvmstarterproject.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.fghilmany.mvvmstarterproject.core.data.DataRepository

class HomeViewModel(private val dataRepository: DataRepository) : ViewModel() {

    fun getStories() = dataRepository.getStories().cachedIn(viewModelScope).asLiveData()

}