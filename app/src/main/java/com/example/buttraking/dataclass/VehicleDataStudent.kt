package com.example.buttraking.dataclass

import com.google.gson.annotations.SerializedName

data class VehicleDataStudent(
    @SerializedName("id") val id: String,
    @SerializedName("latitude") val latitude: String,  // Keep as String for parsing
    @SerializedName("longitude") val longitude: String,
    @SerializedName("driver_type") val driverType: String,
    @SerializedName("school_id") val schoolId: String,
    @SerializedName("timer") val updatedtime: String,
    @SerializedName("vehicle_number") val vehiclenumber: String,
    @SerializedName("driver_id") val driverId: String
) {
    // Convert latitude & longitude safely
    fun getLatitude(): Double? = latitude.toDoubleOrNull()
    fun getLongitude(): Double? = longitude.toDoubleOrNull()
}
