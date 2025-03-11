package com.example.buttraking.model.response

import com.example.buttraking.dataclass.VehicleDataStudent

data class DriverDataResponse(
    val status: Boolean,
    val message: String,
    val data: List<VehicleDataStudent>?
)
