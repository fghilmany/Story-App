package com.fghilmany.mvvmstarterproject.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fghilmany.mvvmstarterproject.core.data.DataRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private var dataRepository: DataRepository) : ViewModel() {

    private lateinit var file: MultipartBody.Part
    private lateinit var description: RequestBody
    private var lat: RequestBody? = null
    private var lon: RequestBody? = null

    fun setStoryParam(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ) {
        this.file = file
        this.description = description
        this.lat = lat
        this.lon = lon
    }

    fun postStory() = dataRepository.addNewStory(file, description, lat, lon).asLiveData()


}