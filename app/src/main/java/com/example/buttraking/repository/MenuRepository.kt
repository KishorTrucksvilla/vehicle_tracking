package com.example.buttraking.repository

import com.example.buttraking.api.RetrofitHelper
import com.example.buttraking.model.response.MenuResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuRepository {

    private val apiService = RetrofitHelper.getApiService()

    // Method to fetch menu data
    fun getMenuStatus(schoolId: String, callback: (MenuResponse?) -> Unit, errorCallback: (String?) -> Unit) {
        val call = apiService.getMenuStatus(schoolId)

        call.enqueue(object : Callback<MenuResponse> {
            override fun onResponse(call: Call<MenuResponse>, response: Response<MenuResponse>) {
                if (response.isSuccessful) {
                    callback(response.body())  // return response to callback
                } else {
                    errorCallback("Error: ${response.message()}")  // handle error case
                }
            }

            override fun onFailure(call: Call<MenuResponse>, t: Throwable) {
                errorCallback(t.message)  // handle failure
            }
        })
    }
}