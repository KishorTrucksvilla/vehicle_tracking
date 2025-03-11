package com.example.buttraking.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.buttraking.repository.GetDriverStudentdataRepository

class GetDriverStudentdataViewModelFactory (private val repository: GetDriverStudentdataRepository):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GetDriverStudentdataViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GetDriverStudentdataViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}