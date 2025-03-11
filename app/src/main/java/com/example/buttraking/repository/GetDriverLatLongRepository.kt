package com.example.buttraking.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.buttraking.api.ApiService
import com.example.buttraking.model.response.GelatandlongResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetDriverLatLongRepository(private val apiService: ApiService) {

    fun getlatlong(schoolId: String,Id: String): LiveData<GelatandlongResponse> {
        val result = MutableLiveData<GelatandlongResponse>()

        // Make the API request
        apiService.getlatandlong(schoolId,Id).enqueue(object : Callback<GelatandlongResponse> {
            override fun onResponse(
                call: Call<GelatandlongResponse>,
                response: Response<GelatandlongResponse>
            ) {
                if (response.isSuccessful) {
                    // Successfully received the data, update the LiveData
                    result.value = response.body()
                } else {
                    // Handle failure scenario (if necessary)
                    result.value = GelatandlongResponse(status = false, Message = "Error fetching data", data = null)
                }
            }

            override fun onFailure(call: Call<GelatandlongResponse>, t: Throwable) {
                // Handle network failure or other errors
                result.value = GelatandlongResponse(status = false, Message = t.message ?: "Unknown error", data = null)
            }
        })

        return result
    }
}

