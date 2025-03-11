package com.example.buttraking.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.buttraking.api.ApiService
import com.example.buttraking.model.response.AddDriver
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PutDriverLatLongRepository(private val apiService: ApiService) {

    fun submitLatLongDriver(driverData: Map<String, String>): LiveData<AddDriver> {
        val driverResponse = MutableLiveData<AddDriver>()

        val call = apiService.Putlatandlong(driverData)
        call.enqueue(object : Callback<AddDriver> {
            override fun onResponse(call: Call<AddDriver>, response: Response<AddDriver>) {
                if (response.isSuccessful) {
                    driverResponse.value = response.body()
                } else {
                    driverResponse.value = AddDriver(false, "API call failed")
                }
            }

            override fun onFailure(call: Call<AddDriver>, t: Throwable) {
                driverResponse.value = AddDriver(false, "Error: ${t.message}")
            }
        })

        return driverResponse
    }
}
