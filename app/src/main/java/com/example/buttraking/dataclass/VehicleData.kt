package com.example.buttraking.dataclass

data class VehicleData(
    val id: Int,  // Ensure this is an Int, not a String
    val latitude: Double,  // Ensure this is a Double, not a String
    val longitude: Double, // Ensure this is a Double, not a String
    val driver_type: String,
    val driver_id: String

)
