package com.example.buttraking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buttraking.model.response.PutHomeWorkResponse
import com.example.buttraking.repository.PutDriverRepository
import kotlinx.coroutines.launch

class PutDriverViewModel(private val repository: PutDriverRepository) : ViewModel() {
    private val _putDriverResponse = MutableLiveData<PutHomeWorkResponse>()
    val putDriverResponse: LiveData<PutHomeWorkResponse> get() = _putDriverResponse

    fun putDriver(driverData: Map<String, String>) {
        viewModelScope.launch {
            repository.putDriver(driverData).observeForever { response ->
                _putDriverResponse.postValue(response)
            }
        }
    }
}
