package com.example.buttraking.model.response

data class MenuResponse(
    val status: Boolean,
    val Message: String,
    val data: List<MenuData>
)

data class MenuData(
    val id: String,
    val all_menu: String,
    val parent_menu: String,
    val status: Boolean,
    val school_id: String
)

