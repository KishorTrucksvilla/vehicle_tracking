package com.example.buttraking.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.buttraking.api.ApiService
import com.example.buttraking.model.response.DeleteResponse
import com.example.buttraking.model.response.GetAdmindataResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetAdmindataRepository(private val apiService: ApiService) {

    fun getAdminData(schoolId: String): LiveData<GetAdmindataResponse> {
        val result = MutableLiveData<GetAdmindataResponse>()

        apiService.GetAdmindata(schoolId).enqueue(object : Callback<GetAdmindataResponse> {
            override fun onResponse(
                call: Call<GetAdmindataResponse>,
                response: Response<GetAdmindataResponse>
            ) {
                if (response.isSuccessful) {
                    result.value = response.body()
                } else {
                    result.value = GetAdmindataResponse(
                        status = false,
                        message = "Failed to retrieve data",
                        data = emptyList()
                    )
                }
            }

            override fun onFailure(call: Call<GetAdmindataResponse>, t: Throwable) {
                result.value = GetAdmindataResponse(
                    status = false,
                    message = t.localizedMessage ?: "Unknown error",
                    data = emptyList()
                )
            }
        })

        return result
    }

    fun deletedriver(schoolId: String, Id: String): LiveData<DeleteResponse> {
        val result = MutableLiveData<DeleteResponse>()

        apiService.deletedriver(schoolId, Id).enqueue(object : Callback<DeleteResponse> {
            override fun onResponse(
                call: Call<DeleteResponse>,
                response: Response<DeleteResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    result.postValue(response.body())
                } else {
                    result.postValue(DeleteResponse("error", "Failed to delete employee"))
                }
            }

            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                result.postValue(DeleteResponse("error", t.message ?: "Unknown error occurred"))
            }
        })
        return result
    }
}