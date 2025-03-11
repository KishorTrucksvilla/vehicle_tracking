package com.example.buttraking.model.response

import com.example.buttraking.dataclass.ClassItem

class ClassDataResponse(
    val status: Boolean,
    val message: String,
    val data: List<ClassItem>
)