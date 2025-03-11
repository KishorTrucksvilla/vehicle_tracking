package com.example.buttraking.model.response

import android.os.Parcel
import android.os.Parcelable
import com.example.buttraking.dataclass.AllEmployee

data class EmployeeResponse(
    val status: Boolean,
    val message: String,
    val employee: List<AllEmployee>
)
data class Employee(
    val id: String,
    val employee_name: String,
    val mobile: String,
    val employee_role: String,
    val picture: String,
    val date_of_joining: String,
    val monthly_salary: String,
    val f_h_name: String,
    val national_id: String,
    val education: String,
    val gender: String,
    val religion: String,
    val blood_group: String,
    val experience: String,
    val date_of_birth: String,
    val home_address: String,
    val school_id: String,
    val created_at: String
): Parcelable {
    constructor(parcel: Parcel) : this(
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
        parcel.writeString(id)
        parcel.writeString(employee_name)
        parcel.writeString(mobile)
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
        parcel.writeString(school_id)
        parcel.writeString(created_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Employee> {
        override fun createFromParcel(parcel: Parcel): Employee {
            return Employee(parcel)
        }

        override fun newArray(size: Int): Array<Employee?> {
            return arrayOfNulls(size)
        }
    }
}
