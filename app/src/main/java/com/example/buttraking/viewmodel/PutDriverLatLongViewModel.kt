package com.example.buttraking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.buttraking.model.response.AddDriver
import com.example.buttraking.repository.PutDriverLatLongRepository

class PutDriverLatLongViewModel(private val repository: PutDriverLatLongRepository) : ViewModel() {
    private val _addDriverResponse = MutableLiveData<AddDriver>()
    val addDriverResponse: LiveData<AddDriver> get() = _addDriverResponse

    fun submitLatLongDriver(driverData: Map<String, String>) {
        repository.submitLatLongDriver(driverData).observeForever { response ->
            _addDriverResponse.postValue(response)
        }
    }
}
