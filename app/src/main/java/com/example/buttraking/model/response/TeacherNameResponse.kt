package com.example.buttraking.model.response

import com.google.gson.annotations.SerializedName

data class TeacherNameResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("Message")
    val message: String,

    @SerializedName("data")
    val data: List<GetTeacherName>
)

data class GetTeacherName(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("employee_name")
    val employeeName: String? = null,

    @SerializedName("employee_role")
    val techer: String? = null
)


