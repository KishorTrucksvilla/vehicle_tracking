package com.example.buttraking.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.buttraking.api.ApiService
import com.example.buttraking.model.response.StudentEditResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditStudentRepository(private val apiService: ApiService) {

    fun updateStudent(data: Map<String, String>): LiveData<Result<StudentEditResponse>> {
        val result = MutableLiveData<Result<StudentEditResponse>>()
        apiService.EditStudent(data).enqueue(object : Callback<StudentEditResponse> {
            override fun onResponse(call: Call<StudentEditResponse>, response: Response<StudentEditResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        result.postValue(Result.success(it))
                    } ?: result.postValue(Result.failure(Exception("Response body is null")))
                } else {
                    result.postValue(Result.failure(Exception("API Error: ${response.code()}")))
                }
            }

            override fun onFailure(call: Call<StudentEditResponse>, t: Throwable) {
                result.postValue(Result.failure(t))
            }
        })
        return result
    }
}
