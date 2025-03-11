package com.example.buttraking.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.buttraking.api.ApiService
import com.example.buttraking.api.RetrofitHelper
import com.example.buttraking.model.response.ClassResponseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClassRepository(private val apiService: ApiService) {

    fun addClass(classData: Map<String, String>): LiveData<ClassResponseData> {
        val classResponse = MutableLiveData<ClassResponseData>()

        val call = apiService.addClasss(classData)

        call.enqueue(object : Callback<ClassResponseData> {
            override fun onResponse(
                call: Call<ClassResponseData>,
                response: Response<ClassResponseData>
            ) {
                if (response.isSuccessful) {
                    classResponse.value = response.body()
                } else {
                    classResponse.value = ClassResponseData(false, response.message())
                }
            }

            override fun onFailure(call: Call<ClassResponseData>, t: Throwable) {
                classResponse.value = ClassResponseData(false, "Error: ${t.message}")
            }
        })

        return classResponse
    }
}
