package com.example.buttraking.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.buttraking.repository.EditStudentRepository
import com.example.buttraking.viewmodel.EditStudentViewModel

class EditStudentViewModelFactory(private val repository: EditStudentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditStudentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditStudentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}