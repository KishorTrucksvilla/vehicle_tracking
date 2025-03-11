package com.example.buttraking.dataclass

import java.io.Serializable

data class ClassItem(
    val class_id: String,
    val class_name: String,
    val total_students: String,
    val class_teacher: String,
    val total_girls: String,
    val tution_fees: String,
    val total_boys: String
): Serializable
