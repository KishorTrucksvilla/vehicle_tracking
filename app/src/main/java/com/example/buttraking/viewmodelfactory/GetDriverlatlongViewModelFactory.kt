package com.example.buttraking.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.buttraking.repository.GetDriverLatLongRepository
import com.example.buttraking.viewmodel.GetDriverLatLongViewModel

class GetDriverlatlongViewModelFactory(private val repository: GetDriverLatLongRepository):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GetDriverLatLongViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GetDriverLatLongViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}