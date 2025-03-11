package com.example.buttraking.viewmodelfactory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.buttraking.model.response.DriverDataResponse
import com.example.buttraking.repository.GetDriverStudentdataRepository

class GetDriverStudentdataViewModel(private val repository: GetDriverStudentdataRepository) : ViewModel() {

    private val _studentLatLong = MutableLiveData<DriverDataResponse>()
    val studentLatLong: LiveData<DriverDataResponse> get() = _studentLatLong

    fun fetchLatLongStudent(schoolId: String, studentId: String) {
        repository.getDriverSgtudent(schoolId, studentId).observeForever { response ->
            _studentLatLong.value = response
        }
    }
}
