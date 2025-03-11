package com.example.buttraking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.buttraking.model.response.DeleteResponse
import com.example.buttraking.model.response.EmployeeResponse
import com.example.buttraking.repository.AllEmployeesRepository

class AllEmployeesViewModel(private val repository: AllEmployeesRepository) : ViewModel() {

    // LiveData to hold the EmployeeResponse
    private val _employeeResponse = MutableLiveData<EmployeeResponse>()
    val employeeResponse: LiveData<EmployeeResponse> get() = _employeeResponse

    // LiveData to hold the total number of employees
    private val _totalEmployeesCount = MutableLiveData<Int>()
    val totalEmployeesCount: LiveData<Int> get() = _totalEmployeesCount

/*
    private val _editEmployeeResponse = MutableLiveData<EditEmpResponse>()
    val editEmployeeResponse: LiveData<EditEmpResponse> get() = _editEmployeeResponse
    val isLoading = MutableLiveData<Boolean>()
    val apiResponse = MutableLiveData<EditEmpResponse>()
*/

    // Function to get all employees and update the total employee count
    fun getAllEmployees(schoolCode: String): LiveData<EmployeeResponse> {
        val employeeResponseLiveData = repository.fetchAllEmployees(schoolCode)

        employeeResponseLiveData.observeForever { response ->
            // Null safety check for 'employee' list
            if (response != null && response.employee != null && response.employee.isNotEmpty()) {
                _totalEmployeesCount.value = response.employee.size
            } else {
                _totalEmployeesCount.value = 0
            }
            _employeeResponse.value = response // Update the full response
        }

        return employeeResponseLiveData
    }

    // Function to delete an employee
    fun deleteEmployee(schoolId: String, employeeId: String): LiveData<DeleteResponse> {
        return repository.deleteEmployee(schoolId, employeeId)
    }

   /* fun editEmployee( employeeData: Map<String, String?>) {
        isLoading.value = true
        // Call repository to edit employee
        val response = repository.editEmployee(employeeData)

        // Observe the result of the repository call
        response.observeForever { result ->
            apiResponse.value = result
            isLoading.value = false
        }
    }*/
}
