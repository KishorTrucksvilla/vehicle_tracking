package com.example.buttraking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.buttraking.model.response.AddDriver
import com.example.buttraking.repository.AddDriverRepository

class AddDriverViewModel(private val repository: AddDriverRepository) : ViewModel() {

    private val _AddDriverResponse = MutableLiveData<AddDriver>()
    val AddDriverResponse: LiveData<AddDriver> get() = _AddDriverResponse

    // Corrected function name and logic
    fun submitAddDriver(addDriverData: Map<String, String>) {
        // Call the repository function to handle the API call
        repository.submitAddDriver(addDriverData).observeForever { response ->
            // Post the response value to LiveData
            _AddDriverResponse.postValue(response)
        }
    }
}
