package com.example.buttraking.repository

import com.example.buttraking.api.ApiService
import com.example.buttraking.model.response.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

 class AddStudentRepository(private val apiService: ApiService) {
    fun addStudent(studentMap: Map<String, String>, onResult: (ApiResponse?, String?) -> Unit) {

        apiService.addStudent(studentMap).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    onResult(response.body(), null)
                } else {
                    onResult(null, response.message())
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                onResult(null, t.message)
            }
        })
    }
}
