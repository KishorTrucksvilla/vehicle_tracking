package com.example.buttraking.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.buttraking.model.response.ClassDataResponse
import com.example.buttraking.repository.AllClassRepository

class AllClassViewModel(private val repository: AllClassRepository) : ViewModel() {

    // Fetch all classes from the repository
    fun getClasses(schoolID: String): MutableLiveData<ClassDataResponse?> {
        return repository.fetchAllClasses(schoolID)
    }

  /*  // Delete class from the repository
    fun deleteClass(schoolId: String, classId: String, context: Context): LiveData<Boolean> {
        return repository.deleteClass(schoolId, classId, context)
    }

    // Update class details in the repository
    fun updateClass(schoolId: String, classId: String, classDetails: Map<String, String>): LiveData<classUpdateResponse> {
        return repository.updateClass(schoolId, classId, classDetails)
    }*/
}




