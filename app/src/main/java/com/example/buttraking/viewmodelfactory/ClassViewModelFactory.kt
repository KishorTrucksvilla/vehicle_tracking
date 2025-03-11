package com.example.buttraking.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.buttraking.repository.ClassRepository
import com.example.buttraking.viewmodel.ClassViewModel

class ClassViewModelFactory(private val repository: ClassRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClassViewModel::class.java)) {
            return ClassViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
