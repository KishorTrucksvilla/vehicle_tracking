package com.example.buttraking.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.buttraking.repository.getDriverdataRepository
import com.example.buttraking.viewmodel.getDriverdataViewModel

class getdriverdataViewModelFactory (private val repository: getDriverdataRepository):
    ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(getDriverdataViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return getDriverdataViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}