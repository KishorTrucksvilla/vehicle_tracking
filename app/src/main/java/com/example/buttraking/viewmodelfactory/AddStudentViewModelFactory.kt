package com.example.buttraking.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.buttraking.repository.AddStudentRepository
import com.example.buttraking.viewmodel.AddStudentViewModel

class AddStudentViewModelFactory(private val repository: AddStudentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddStudentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddStudentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
