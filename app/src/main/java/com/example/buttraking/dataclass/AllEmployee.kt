package com.example.buttraking.dataclass

import android.os.Parcel
import android.os.Parcelable

data class AllEmployee(
    val name: String,
    val title: String,
    val img: Int,
    val id: String,
    val employee_name: String,
    val employee_id: String,
    val number: String,
    val employee_role: String,
    val picture: String?,
    val date_of_joining: String,
    val monthly_salary: String?,
    val f_h_name: String,
    val national_id: String,
    val education: String,
    val gender: String,
    val religion: String,
    val blood_group: String,
    val experience: String,
    val date_of_birth: String,
    val home_address: String,
    val email: String,
    val username: String,
    val password: String,
    val st_status: String,
    val school_id: String,
    val created_at: String,
    val school_name:String,
    val institute_logo:String
) : Parcelable
{
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(title)
        parcel.writeInt(img)
        parcel.writeString(id)
        parcel.writeString(employee_name)
        parcel.writeString(employee_id)
        parcel.writeString(number)
        parcel.writeString(employee_role)
        parcel.writeString(picture)
        parcel.writeString(date_of_joining)
        parcel.writeString(monthly_salary)
        parcel.writeString(f_h_name)
        parcel.writeString(national_id)
        parcel.writeString(education)
        parcel.writeString(gender)
        parcel.writeString(religion)
        parcel.writeString(blood_group)
        parcel.writeString(experience)
        parcel.writeString(date_of_birth)
        parcel.writeString(home_address)
        parcel.writeString(email)
        parcel.writeString(username)
        parcel.writeString(password)
        parcel.writeString(st_status)
        parcel.writeString(school_id)
        parcel.writeString(created_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AllEmployee> {
        override fun createFromParcel(parcel: Parcel): AllEmployee {
            return AllEmployee(parcel)
        }

        override fun newArray(size: Int): Array<AllEmployee?> {
            return arrayOfNulls(size)
        }
    }
}
