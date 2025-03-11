package com.example.buttraking.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.buttraking.repository.AddDriverRepository
import com.example.buttraking.viewmodel.AddDriverViewModel

class AddDriverViewModelFactory(private val repository: AddDriverRepository): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddDriverViewModel::class.java)) {
            return AddDriverViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}