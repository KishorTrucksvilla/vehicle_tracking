package com.example.buttraking.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.buttraking.api.ApiService
import com.example.buttraking.api.RetrofitHelper
import com.example.buttraking.model.response.AddDriver
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddDriverRepository(private val apiService: ApiService) {


    fun submitAddDriver(DriverData: Map<String, String>): LiveData<AddDriver> {
        val DriverResponse = MutableLiveData<AddDriver>()

        val apiService = RetrofitHelper.getApiService()
        val call = apiService.Addriver(DriverData)

        call.enqueue(object : Callback<AddDriver> {
            override fun onResponse(call: Call<AddDriver>, response: Response<AddDriver>) {
                if (response.isSuccessful) {
                    DriverResponse.value = response.body()
                } else {
                    DriverResponse.value = AddDriver(false, "API call failed")
                }
            }


            override fun onFailure(call: Call<AddDriver>, t: Throwable) {
                DriverResponse.value = AddDriver(false, "Error: ${t.message}")
            }
        })

        return DriverResponse
    }
}

