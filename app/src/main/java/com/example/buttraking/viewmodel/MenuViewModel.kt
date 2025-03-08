package com.example.buttraking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.buttraking.model.response.MenuResponse
import com.example.buttraking.repository.MenuRepository

class MenuViewModel : ViewModel() {
    private val menuRepository = MenuRepository()

    // LiveData to observe the API response
    private val _menuData = MutableLiveData<MenuResponse>()
    val menuData: LiveData<MenuResponse> get() = _menuData

    // LiveData to observe errors
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // Method to fetch the menu data
    fun fetchMenuData(schoolId: String) {
        menuRepository.getMenuStatus(schoolId, {
            _menuData.postValue(it)  // Set response data to LiveData
        }, {
            _errorMessage.postValue(it)  // Set error message
        })
    }

}