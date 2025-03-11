package com.example.buttraking.model.response

import com.example.buttraking.dataclass.DriverData

class DirverdataResponse(
    val status: Boolean,
    val data: List<DriverData>
)