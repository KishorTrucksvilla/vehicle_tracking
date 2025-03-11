package com.example.buttraking.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.buttraking.repository.GetAdmindataRepository
import com.example.buttraking.viewmodel.GetAdmindataViewModel

class ViewModelFactoryAdmindata(private val repository: GetAdmindataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GetAdmindataViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GetAdmindataViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}