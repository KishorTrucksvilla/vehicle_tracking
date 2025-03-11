package com.example.buttraking.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.buttraking.repository.PutDriverLatLongRepository
import com.example.buttraking.viewmodel.PutDriverLatLongViewModel

class PutDriverLatLongViewModelFactory(private val repository: PutDriverLatLongRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PutDriverLatLongViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PutDriverLatLongViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
