package com.example.buttraking.repository

import com.example.buttraking.api.ApiService
import com.example.buttraking.model.response.LoginResponse
import retrofit2.Call

class UserRepository(private val api: ApiService) {
    fun login(
        schoolId: String,
        password: String
    ):
            Call<LoginResponse> {
        return api.login(schoolId, password)
    }
}
