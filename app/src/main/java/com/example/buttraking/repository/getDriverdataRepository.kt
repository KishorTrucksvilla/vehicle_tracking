package com.example.buttraking.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.buttraking.api.ApiService
import com.example.buttraking.model.response.DirverdataResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class getDriverdataRepository(private val apiService: ApiService) {

    fun getdriverdata(schoolId: String): LiveData<DirverdataResponse> {
        val result = MutableLiveData<DirverdataResponse>()

        // Make the API request
        apiService.getDirverdata(schoolId).enqueue(object : Callback<DirverdataResponse> {
            override fun onResponse(
                call: Call<DirverdataResponse>,
                response: Response<DirverdataResponse>
            ) {
                if (response.isSuccessful) {
                    // Successfully received the data, update the LiveData
                    result.value = response.body()
                } else {
                    // Handle failure scenario (if necessary)
                    result.value = DirverdataResponse(status = false, data = emptyList())
                }
            }

            override fun onFailure(call: Call<DirverdataResponse>, t: Throwable) {
                // Handle network failure or other errors
                result.value = DirverdataResponse(
                    status = false,
                    data = emptyList()
                )
            }
        })

        return result
    }

}