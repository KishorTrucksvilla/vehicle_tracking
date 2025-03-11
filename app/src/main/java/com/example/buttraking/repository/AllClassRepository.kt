package com.example.buttraking.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.buttraking.api.RetrofitHelper
import com.example.buttraking.helper.MethodLibrary
import com.example.buttraking.model.response.ClassDataResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllClassRepository {

    val toolBox = MethodLibrary()

    private val apiService = RetrofitHelper.getApiService()

    // Fetch all classes from the API
    fun fetchAllClasses(schoolId: String): MutableLiveData<ClassDataResponse?> {
        val classData = MutableLiveData<ClassDataResponse?>()

        apiService.getClasses(schoolId).enqueue(object : Callback<ClassDataResponse> {
            override fun onResponse(
                call: Call<ClassDataResponse>,
                response: Response<ClassDataResponse>
            ) {
                if (response.isSuccessful) {
                    classData.value = response.body()
                } else {
                    classData.value = null
                }
            }

            override fun onFailure(call: Call<ClassDataResponse>, t: Throwable) {
                classData.value = null
            }
        })
        return classData
    }
}
    // Delete class from the API
   /* fun deleteClass(schoolId: String, classId: String, context: Context): LiveData<Boolean> {
        val deleteResponse = MutableLiveData<Boolean>()

        apiService.deleteClass(schoolId, classId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                deleteResponse.value = response.isSuccessful
                if (!response.isSuccessful) {
                    toolBox.showConfirmationDialog(
                        context = context,
                        title = "Class Deletion Unsuccessful    ",
                        message = "To delete this class, please ensure all students are transferred to another active class.",
                        positiveButtonText = "ok",
                        positiveButtonAction = { },
                        negativeButtonText = "",
                        negativeButtonAction = { },
                        cancelable = true
                    )
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                deleteResponse.value = false
            }
        })

        return deleteResponse
    }


    fun updateClass(schoolId: String, classId: String, classDetails: Map<String, String>): LiveData<classUpdateResponse> {
        val classUpdateResponse = MutableLiveData<classUpdateResponse>()

        apiService.updateClass(schoolId, classId, classDetails).enqueue(object :
            Callback<classUpdateResponse> {
            override fun onResponse(call: Call<classUpdateResponse>, response: Response<classUpdateResponse>) {
                if (response.isSuccessful) {
                    classUpdateResponse.value = response.body()
                } else {
                    // Handle unsuccessful response
                    classUpdateResponse.value = classUpdateResponse(false, "Failed to update class")
                }
            }

            override fun onFailure(call: Call<classUpdateResponse>, t: Throwable) {
                // Handle failure (e.g., network issues)
                classUpdateResponse.value = classUpdateResponse(false, t.localizedMessage ?: "Unknown error")
            }
        })

        return classUpdateResponse
    }
}


*/
