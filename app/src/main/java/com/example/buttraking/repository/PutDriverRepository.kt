package com.example.buttraking.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.buttraking.api.ApiService
import com.example.buttraking.model.response.PutHomeWorkResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PutDriverRepository(private val apiService: ApiService) {

    fun putDriver(driverData: Map<String, String>): LiveData<PutHomeWorkResponse> {
        val result = MutableLiveData<PutHomeWorkResponse>()

        apiService.putDriver(driverData).enqueue(object : Callback<PutHomeWorkResponse> {
            override fun onResponse(
                call: Call<PutHomeWorkResponse>,
                response: Response<PutHomeWorkResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    result.value = response.body()
                } else {
                    result.value = PutHomeWorkResponse(
                        status = "false",
                        message = "Error: ${response.code()} - ${response.message()}"
                    )
                }
            }

            override fun onFailure(call: Call<PutHomeWorkResponse>, t: Throwable) {
                result.value = PutHomeWorkResponse(
                    status = "false",
                    message = t.message ?: "Unknown error occurred"
                )
            }
        })

        return result
    }
}
