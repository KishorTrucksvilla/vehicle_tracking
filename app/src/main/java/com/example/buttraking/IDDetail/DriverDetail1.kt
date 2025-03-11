package com.example.buttraking.IDDetail

import android.content.Context
import android.content.SharedPreferences

class DriverDetail1 {


    private lateinit var sharedPreferences: SharedPreferences

    fun getName(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("driver_name", null) ?: ""
    }

    fun getSchoolName(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("school_name", null) ?: ""
    }

    fun getSchoolLogo(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("institute_logo", null) ?: ""
    }

    fun getFatherName(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("f_h_name", null) ?: ""
    }

    fun getRole(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("role", null) ?: ""
    }

    fun Driverid(context: Context): String {
        sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("id", null) ?: ""

    }
}