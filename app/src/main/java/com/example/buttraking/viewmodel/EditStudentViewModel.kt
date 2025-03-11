package com.example.buttraking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.buttraking.model.response.StudentEditResponse
import com.example.buttraking.repository.EditStudentRepository

class EditStudentViewModel(private val repository: EditStudentRepository) : ViewModel() {
    private val _editStudentResult = MutableLiveData<Result<StudentEditResponse>>()
    val editStudentResult: LiveData<Result<StudentEditResponse>> = _editStudentResult

    fun editStudent(data: Map<String, String>) {
        repository.updateStudent(data).observeForever { result ->
            _editStudentResult.postValue(result)
        }
    }
}
