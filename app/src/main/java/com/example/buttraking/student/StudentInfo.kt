package com.example.buttraking.student

import android.content.Context
import android.content.SharedPreferences

class StudentInfo {
    private lateinit var sharedPreferences: SharedPreferences

    fun getStudentName(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("st_name", null) ?: ""
    }

    fun getStudentSchoolLogo(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("institute_logo", null) ?: ""
    }

    fun getStudentSchoolName(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("school_name", null) ?: ""
    }

    fun getStudentPicture(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("picture", null) ?: ""
    }

    fun getStudentRegistrationId(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("registration_no", null) ?: ""
    }

    fun getStudentClass(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("st_class", null) ?: ""
    }

    fun getStudentDtofAdd(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("dt_of_admission", null) ?: ""
    }

    fun getStudentStatus(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("st_status", null) ?: ""
    }

    fun getStudentDtofBirth(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("dt_of_birth", null) ?: ""
    }

    fun getStudentDiscountFee(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("discount_fee", null) ?: ""
    }

    fun getStudentPhoneNumber(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("number", null) ?: ""
    }

    fun getStudentReligion(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("religion", null) ?: ""
    }

    fun getStudentCaste(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("cast", null) ?: ""
    }

    fun getStudentGender(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("gender", null) ?: ""
    }

    fun getStudentBloodGroup(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("blood_group", null) ?: ""
    }


    fun getStudentDisease(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("disease_if_any", null) ?: ""
    }

    fun getStudentFamily(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("family", null) ?: ""
    }

    fun getStudentSiblings(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("siblings", null) ?: ""
    }

    fun getOrphanStudent(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("orphan_student", null) ?: ""
    }

    fun getStudentOsc(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("osc", null) ?: ""
    }

    fun getStudentPreviousSchool(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("st_previous_school", null) ?: ""
    }

    fun getStudentPreviousId(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("previous_id", null) ?: ""
    }

    fun getstudentIdentificationMark(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("identification_mark", null) ?: ""
    }

    fun getStudentAddress(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("address", null) ?: ""
    }

    fun getStudentFatherName(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("father_name", null) ?: ""
    }

    fun getStudentFatherId(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("father_id", null) ?: ""
    }

    fun getFatherEducation(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("father_education", null) ?: ""
    }

    fun getFatherMobile(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("father_mobile", null) ?: ""
    }

    fun getFatherProfession(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("father_profession", null) ?: ""
    }

    fun getFatherOccupation(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("father_occupation", null) ?: ""
    }

    fun getFatherIncome(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("father_income", null) ?: ""
    }

    fun getStudentMotherName(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("mother_name", null) ?: ""
    }

    fun getStudentMotherId(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("mother_id", null) ?: ""
    }

    fun getMotherEducation(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("mother_education", null) ?: ""
    }

    fun getMotherMobile(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("mother_mobile", null) ?: ""
    }


    fun getMotherProfession(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("mother_profession", null) ?: ""
    }

    fun getMotherOccupation(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("mother_occupation", null) ?: ""
    }

    fun getStudentActiveStutus(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("st_status", null) ?: ""
    }

    fun getMotherIncome(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("mother_income", null) ?: ""
    }


    fun getHomeworkItemCount(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences("HomeworkPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("itemCount", 0) // Returns 0 if the value is not found
    }

    fun getStudentId(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("student_id", null)?:""
    }

}