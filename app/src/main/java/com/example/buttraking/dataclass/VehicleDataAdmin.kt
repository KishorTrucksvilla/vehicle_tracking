package com.example.buttraking.dataclass

import android.os.Parcel
import android.os.Parcelable

data class VehicleDataAdmin(
    val id: String,
    val driver_name: String,
    val vehicle_number: String,
    val driver_type: String,
    val number: String,
    val latitude: String?,  // Nullable
    val longitude: String?, // Nullable
    val role: String,
    val username: String,
    val password: String,
    val school_id: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,  // Non-null fields
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),   // Nullable latitude
        parcel.readString(),   // Nullable longitude
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(driver_name)
        parcel.writeString(driver_type)
        parcel.writeString(number)
        parcel.writeString(latitude ?: "")  // Ensuring safe handling of null
        parcel.writeString(longitude ?: "")
        parcel.writeString(role)
        parcel.writeString(username)
        parcel.writeString(password)
        parcel.writeString(school_id)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<VehicleDataAdmin> {
        override fun createFromParcel(parcel: Parcel): VehicleDataAdmin {
            return VehicleDataAdmin(parcel)
        }

        override fun newArray(size: Int): Array<VehicleDataAdmin?> {
            return arrayOfNulls(size)
        }
    }
}
