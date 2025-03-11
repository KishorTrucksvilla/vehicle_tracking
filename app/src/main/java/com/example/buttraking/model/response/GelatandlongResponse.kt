package com.example.buttraking.model.response

import com.example.buttraking.dataclass.VehicleData

class GelatandlongResponse(
    val status: Boolean,
    val Message: String,
    val data: List<VehicleData>?

)