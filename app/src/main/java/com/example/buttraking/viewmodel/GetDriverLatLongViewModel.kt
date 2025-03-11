package com.example.buttraking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.buttraking.model.response.GelatandlongResponse
import com.example.buttraking.repository.GetDriverLatLongRepository

class GetDriverLatLongViewModel(private val repository: GetDriverLatLongRepository): ViewModel() {

    private val _latlong = MutableLiveData<GelatandlongResponse>()
    val latlong: LiveData<GelatandlongResponse> get() = _latlong

    fun fetchlatlong(schoolId: String,ID: String) {
        repository.getlatlong(schoolId,ID).observeForever { response ->
            _latlong.value = response
        }
    }
}