package com.example.buttraking.model.response

import java.util.List

data class AllClassNameResponse(
    val status: Boolean,
    val classes: List<ClassDetails>
)

data class ClassDetails(
    val id: String,
    val class_name: String,
    val tution_fees: String,
    val class_teacher: String,
    val school_id: String
)