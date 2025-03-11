package com.example.buttraking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.buttraking.model.response.ApiResponse
import com.example.buttraking.repository.EmployeeRepository

class AddNewEmployeesViewModel(private val repository: EmployeeRepository) : ViewModel() {

    val apiResponse: LiveData<ApiResponse> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun addNewEmployee(employeeData: Map<String, Any?>) {
        isLoading.value = true
        val response = repository.addEmployee(employeeData)

        response.observeForever { result ->
            (apiResponse as MutableLiveData).value = result
            isLoading.value = false
        }
    }
}
