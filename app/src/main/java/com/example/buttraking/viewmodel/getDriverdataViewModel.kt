package com.example.buttraking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.buttraking.model.response.DirverdataResponse
import com.example.buttraking.repository.getDriverdataRepository

class getDriverdataViewModel(private val repository: getDriverdataRepository): ViewModel() {
    private val _latlong = MutableLiveData<DirverdataResponse>()
    val driverdata: LiveData<DirverdataResponse> get() = _latlong

    fun fetchDriverdata(schoolId: String) {
        repository.getdriverdata(schoolId).observeForever { response ->
            _latlong.value = response
        }
    }

}