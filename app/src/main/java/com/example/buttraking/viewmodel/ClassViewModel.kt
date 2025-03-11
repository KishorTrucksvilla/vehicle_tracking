package com.example.buttraking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.buttraking.model.response.ClassResponseData
import com.example.buttraking.repository.ClassRepository

class ClassViewModel(private val repository: ClassRepository) : ViewModel() {

    fun addClass(classData: Map<String, String>): LiveData<ClassResponseData> {
        return repository.addClass(classData)
    }
}