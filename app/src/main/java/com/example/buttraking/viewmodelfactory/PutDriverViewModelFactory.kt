package com.example.buttraking.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.buttraking.repository.PutDriverRepository
import com.example.buttraking.viewmodel.PutDriverViewModel

class PutDriverViewModelFactory(private val repository: PutDriverRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PutDriverViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PutDriverViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
