package com.example.buttraking.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.buttraking.api.ApiService
import com.example.buttraking.model.response.DriverDataResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetDriverStudentdataRepository(private val apiService: ApiService) {
    fun getDriverSgtudent(schoolId: String, studentId: String): LiveData<DriverDataResponse> {
        val result = MutableLiveData<DriverDataResponse>()

        Log.d("API Request", "Fetching data for School ID: $schoolId, Student ID: $studentId")

        apiService.getDriverStudentData(schoolId, studentId)
            .enqueue(object : Callback<DriverDataResponse> {
                override fun onResponse(
                    call: Call<DriverDataResponse>,
                    response: Response<DriverDataResponse>
                ) {
                    Log.d(
                        "API Response",
                        "Response Code: ${response.code()}, Body: ${response.body()}"
                    )

                    if (response.isSuccessful && response.body() != null) {
                        result.postValue(response.body())
                    } else {
                        Log.e("API Response", "Failed: ${response.errorBody()?.string()}")
                        result.postValue(
                            DriverDataResponse(
                                status = false,
                                message = "Failed to retrieve data",
                                data = emptyList()
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<DriverDataResponse>, t: Throwable) {
                    Log.e("API Failure", "Error: ${t.localizedMessage}")
                    result.postValue(
                        DriverDataResponse(
                            status = false,
                            message = t.localizedMessage ?: "Unknown error",
                            data = emptyList()
                        )
                    )
                }
            })

        return result
    }

}