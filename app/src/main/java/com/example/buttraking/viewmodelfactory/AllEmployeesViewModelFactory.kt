package com.example.buttraking.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.buttraking.repository.AllEmployeesRepository
import com.example.buttraking.viewmodel.AllEmployeesViewModel

class AllEmployeesViewModelFactory(private val repository: AllEmployeesRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllEmployeesViewModel::class.java)) {
            return AllEmployeesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
