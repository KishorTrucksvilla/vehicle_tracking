package com.example.buttraking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.buttraking.dataclass.Student
import com.example.buttraking.repository.getStudentRepository

class getStudentViewModel(private val repository: getStudentRepository) : ViewModel() {

    private val _students = MutableLiveData<List<Student>>()
    val students: LiveData<List<Student>> get() = _students

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchStudentsBySchoolId(schoolId: String) {
        val params = mapOf("school_id" to schoolId)
        repository.getStudentData(params, schoolId) { response ->
            if (response != null && response.status) {
                _students.postValue(response.data)
            } else {
                _errorMessage.postValue("Failed to load student data")
            }
        }
    }

    private val _deleteStatus = MutableLiveData<Boolean>()
    val deleteStatus: LiveData<Boolean> get() = _deleteStatus

    fun deleteStudent(schoolId: String, studentId: String) {
        repository.deleteStudent(schoolId, studentId) { success ->
            _deleteStatus.postValue(success)
        }
    }
}
