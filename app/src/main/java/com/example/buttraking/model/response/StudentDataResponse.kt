package com.example.buttraking.model.response

import com.example.buttraking.dataclass.Student

data class StudentDataResponse(
    val status: Boolean,
    val message: String,
    val data: List<Student>
)
