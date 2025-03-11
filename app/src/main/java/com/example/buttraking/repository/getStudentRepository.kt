package com.example.buttraking.repository

import com.example.buttraking.api.ApiService
import com.example.buttraking.model.response.StudentDataResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class getStudentRepository(private val apiService: ApiService) {

    fun getStudentData(params: Map<String, String>, SchooId: String, callback: (StudentDataResponse?) -> Unit) {

        val call = apiService.getStudent(SchooId,params)
        call.enqueue(object : Callback<StudentDataResponse> {
            override fun onResponse(call: Call<StudentDataResponse>, response: Response<StudentDataResponse>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<StudentDataResponse>, t: Throwable) {
                callback(null)
            }
        })
    }
    fun deleteStudent(schoolId: String, studentId: String, callback: (Boolean) -> Unit)
    {
        val call = apiService.deleteStudent(schoolId, studentId)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }

}
