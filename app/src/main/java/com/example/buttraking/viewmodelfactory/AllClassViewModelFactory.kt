package com.example.buttraking.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.buttraking.repository.AllClassRepository
import com.example.buttraking.viewmodel.AllClassViewModel

class AllClassViewModelFactory(private val repository: AllClassRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AllClassViewModel(repository) as T
    }
}
