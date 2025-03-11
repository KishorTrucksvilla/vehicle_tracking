package com.example.buttraking.model.response

import com.example.buttraking.dataclass.VehicleDataAdmin

class GetAdmindataResponse
    (  val status: Boolean,
       val message: String,
       val data: List<VehicleDataAdmin>
)
