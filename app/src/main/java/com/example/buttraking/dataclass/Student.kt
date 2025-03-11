package com.example.buttraking.dataclass

import android.os.Parcel
import android.os.Parcelable

data class Student(
    val id: String,
    val school_id: String,
    val st_name: String,
    val class_name: String,
    val registration_no: String,
    val student_id: String,
    val attendance_status: String,
    val status:String,
    val st_status:String,
    val st_class: String,
    val institute_logo: String,
    val picture: String,
    val dt_of_admission: String,
    val discount_fee: String,
    val username:String,
    val password :String,
    val dt_of_birth: String,
    val religion: String,
    val blood_group: String,
    val orphan_student: String,
    val gender: String,
    val cast: String,
    val osc: String,
    val previous_id: String,
    val family: String,
    val disease_if_any: String,
    val additional_note: String,
    val siblings: String,
    val address: String,
    val father_name: String,
    val father_id: String,
    val father_mobile: String,
    val father_education: String,
    val father_occupation: String,
    val father_profession: String,
    val st_birth_id: String,
    val father_income: String,
    val school_name: String,
    val mother_name: String,
    val mother_id: String,
    val mother_education: String,
    val mother_mobile:String,
    val mother_occupation: String,
    val mother_profession: String,
    val identification_mark: String,
    val st_previous_school: String,
    val mother_income: String,
    val number: String,
    val created_at: String
) : Parcelable {
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
        parcel.writeString(school_id)
        parcel.writeString(st_name)
        parcel.writeString(username)
        parcel.writeString(password)
        parcel.writeString(registration_no)
        parcel.writeString(st_class)
        parcel.writeString(picture)
        parcel.writeString(status)
        parcel.writeString(dt_of_admission)
        parcel.writeString(discount_fee)
        parcel.writeString(number)
        parcel.writeString(dt_of_birth)
        parcel.writeString(religion)
        parcel.writeString(blood_group)
        parcel.writeString(orphan_student)
        parcel.writeString(gender)
        parcel.writeString(cast)
        parcel.writeString(osc)
        parcel.writeString(previous_id)
        parcel.writeString(st_previous_school)
        parcel.writeString(identification_mark)
        parcel.writeString(family)
        parcel.writeString(disease_if_any)
        parcel.writeString(additional_note)
        parcel.writeString(siblings)
        parcel.writeString(address)
        parcel.writeString(father_name)
        parcel.writeString(father_id)
        parcel.writeString(father_education)
        parcel.writeString(father_mobile)
        parcel.writeString(father_occupation)
        parcel.writeString(father_profession)
        parcel.writeString(father_income)
        parcel.writeString(mother_name)
        parcel.writeString(mother_id)
        parcel.writeString(mother_education)
        parcel.writeString(mother_mobile)
        parcel.writeString(mother_occupation)
        parcel.writeString(mother_profession)
        parcel.writeString(mother_income)
        parcel.writeString(created_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Student> {
        override fun createFromParcel(parcel: Parcel): Student {
            return Student(parcel)
        }

        override fun newArray(size: Int): Array<Student?> {
            return arrayOfNulls(size)
        }
    }
}