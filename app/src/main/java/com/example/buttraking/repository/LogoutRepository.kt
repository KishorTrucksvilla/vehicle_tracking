package com.example.buttraking.repository

import com.example.buttraking.api.ApiService
import com.example.buttraking.model.response.LogoutResponse
import retrofit2.Call

class LogoutRepository(private val apiService: ApiService) {

    // The repository makes the API call and returns the response as a Call
    fun logout(): Call<LogoutResponse> {
        return apiService.logout()
    }
}
