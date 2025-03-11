package com.example.buttraking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buttraking.model.response.DeleteResponse
import com.example.buttraking.model.response.GetAdmindataResponse
import com.example.buttraking.repository.GetAdmindataRepository
import kotlinx.coroutines.launch

class GetAdmindataViewModel(private val repository: GetAdmindataRepository) : ViewModel() {
    private val _latlong = MutableLiveData<GetAdmindataResponse>()
    val Adminrdata: LiveData<GetAdmindataResponse> get() = _latlong
/*
    private val _timeTableData = MutableLiveData<Result<GetTimeTableResponse>>()
    val timeTableData: LiveData<Result<GetTimeTableResponse>> get() = _timeTableData

*/
    private val _deleteResult = MutableLiveData<Result<DeleteResponse>>()
    val deleteResult: LiveData<Result<DeleteResponse>> get() = _deleteResult


    fun fetcAdmindata(schoolId: String) {
        repository.getAdminData(schoolId).observeForever { response ->
            _latlong.value = response
        }
    }

    fun deletedriver(schoolId: String, employeeId: String): LiveData<DeleteResponse> {
        val resultLiveData = MutableLiveData<DeleteResponse>()
        viewModelScope.launch {
            repository.deletedriver(schoolId, employeeId).observeForever { response ->
                response?.let {
                    resultLiveData.postValue(it)
                }
            }
        }
        return resultLiveData
    }
}