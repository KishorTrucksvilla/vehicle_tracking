package com.example.buttraking.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.buttraking.api.ApiService
import com.example.buttraking.api.RetrofitHelper
import com.example.buttraking.model.response.DeleteResponse
import com.example.buttraking.model.response.EmployeeResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllEmployeesRepository(apiService: ApiService) {
    private val apiService: ApiService = RetrofitHelper.getApiService()
    fun fetchAllEmployees(schoolCode: String): LiveData<EmployeeResponse> {
        val employeeData = MutableLiveData<EmployeeResponse>()
        apiService.getEmployees(schoolCode).enqueue(object : Callback<EmployeeResponse> {
            override fun onResponse(
                call: Call<EmployeeResponse>,
                response: Response<EmployeeResponse>
            ) {
                if (response.isSuccessful) {
                    employeeData.value = response.body()
                }
            }

            override fun onFailure(call: Call<EmployeeResponse>, t: Throwable) {
                // Log error and handle failure
                t.printStackTrace()
            }
        })
        return employeeData
    }


    fun deleteEmployee(schoolId: String, employeeId: String): LiveData<DeleteResponse> {
        val deleteResponse = MutableLiveData<DeleteResponse>()

        // Make API call to delete employee
        apiService.deleteEmployee(schoolId, employeeId).enqueue(object : Callback<DeleteResponse> {
            override fun onResponse(call: Call<DeleteResponse>, response: Response<DeleteResponse>) {
                if (response.isSuccessful) {
                    deleteResponse.value = response.body() // Pass the successful response
                } else {
                    deleteResponse.value = DeleteResponse(status = "failure", message = "Deletion failed")
                }
            }

            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                deleteResponse.value = DeleteResponse(status = "failure", message = "Error: ${t.message}")
            }
        })

        return deleteResponse
    }
/*
    fun editEmployee(employeeData: Map<String, String?>): LiveData<EditEmpResponse> {
        val liveData = MutableLiveData<EditEmpResponse>()

        apiService.EditEmployee(employeeData).enqueue(object : Callback<EditEmpResponse> {
            override fun onResponse(call: Call<EditEmpResponse>, response: Response<EditEmpResponse>) {
                if (response.isSuccessful) {
                    liveData.value = response.body()
                } else {
                    liveData.value = EditEmpResponse(status = "failure", message = "Update failed.")
                }
            }

            override fun onFailure(call: Call<EditEmpResponse>, t: Throwable) {
                liveData.value = EditEmpResponse(status = "failure", message = "Error: ${t.message}")
            }
        })

        return liveData
    }
*/
}
