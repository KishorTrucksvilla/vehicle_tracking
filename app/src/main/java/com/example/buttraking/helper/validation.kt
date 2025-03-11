package com.example.buttraking.helper

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.res.ResourcesCompat
import com.example.buttraking.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class validation {

    fun validation(
        editText: EditText,
    ):Boolean{
        if (editText.text.isNotEmpty() == true){
            return true
        }else{
            return false
        }
    }

    fun SpinnerValidation(
        spinner: Spinner
    ): Boolean {

        if (!spinner.selectedItem.toString().equals(spinner.selectedItem.toString())){
            return true
        }else{
            return false
        }
    }

    fun validation(
        editText: EditText,
        editText2: EditText,
    ):Boolean{
        if (
            editText.text.isNotEmpty() &&
            editText2.text.isNotEmpty())
        {
            return true
        }else{
            return false
        }
    }

    fun validation(
        editText: EditText,
        editText2: EditText,
        editText3: EditText,
    ):Boolean{
        if (
            editText.text.isNotEmpty() && editText2.text.isNotEmpty() && editText3.text.isNotEmpty()
        )
        {
            return true
        }else{
            return false
        }
    }

    fun validation(
        editText: EditText,
        editText2: EditText,
        editText3: EditText,
        editText4: EditText
    ):Boolean{
        if (
            editText.text.isNotEmpty() && editText2.text.isNotEmpty() && editText3.text.isNotEmpty()
        )
        {
            return true
        }else{
            return false
        }
    }

    fun validation(
        editText: EditText,
        editText2: EditText,
        editText3: EditText,
        editText4: EditText,
        editText5: EditText
    ):Boolean{
        if (
            editText.text.isNotEmpty() && editText2.text.isNotEmpty() && editText3.text.isNotEmpty()
        )
        {

            return true
        }else{
            return false
        }
    }

    fun validation(
        editText: EditText,
        editText2: EditText,
        editText3: EditText,
        editText4: EditText,
        editText5: EditText,
        editText6: EditText
    ):Boolean{
        if (
            editText.text.isNotEmpty() && editText2.text.isNotEmpty() && editText3.text.isNotEmpty()
        )
        {
            return true
        }else{
            return false
        }
    }

    fun validation(
        editText: EditText,
        editText2: EditText,
        editText3: EditText,
        editText4: EditText,
        editText5: EditText,
        editText6: EditText,
        editText7: EditText
    ):Boolean{
        if (
            editText.text.isNotEmpty() && editText2.text.isNotEmpty() && editText3.text.isNotEmpty()
        )
        {
            return true
        }else{
            return false
        }
    }

    fun validation(
        editText: EditText,
        editText2: EditText,
        editText3: EditText,
        editText4: EditText,
        editText5: EditText,
        editText6: EditText,
        editText7: EditText,
        editText8: EditText
    ):Boolean{
        if (
            editText.text.isNotEmpty() && editText2.text.isNotEmpty() && editText3.text.isNotEmpty()
        )
        {
            return true
        }else{
            return false
        }
    }

    fun validation(
        editText: EditText,
        editText2: EditText,
        editText3: EditText,
        editText4: EditText,
        editText5: EditText,
        editText6: EditText,
        editText7: EditText,
        editText8: EditText,
        editText9: EditText
    ):Boolean{
        if (
            editText.text.isNotEmpty() && editText2.text.isNotEmpty() && editText3.text.isNotEmpty()
        )
        {
            return true
        }else{
            return false
        }
    }

    fun validation(
        editText: EditText,
        editText2: EditText,
        editText3: EditText,
        editText4: EditText,
        editText5: EditText,
        editText6: EditText,
        editText7: EditText,
        editText8: EditText,
        editText9: EditText,
        editText10: EditText,
    ):Boolean{
        if (
            editText.text.isNotEmpty() &&
            editText2.text.isNotEmpty() &&
            editText3.text.isNotEmpty()
        )
        {
            return true
        }else{
            return false
        }
    }

    fun errrMs(
        editText: TextInputEditText,
        text: String
    ){
        editText.error = text
    }




    fun errorLayout (layout: TextInputLayout, massage : String)
    {
        layout.error = massage
    }

    fun spinnerValidation(
        LayoutId : LinearLayoutCompat,
        RemoveColor : Boolean
    ){
        if (RemoveColor){
          //  ResourcesCompat.getDrawable(LayoutId.resources, R.drawable.spinner_validation, null);
            val background = LayoutId.background
            if (background is GradientDrawable) {
                background.setStroke(4, Color.parseColor("#FF5733"))
                background.cornerRadius = 16f
            }
        }
    }




    fun updateSpinnerValidation(LayoutId: LinearLayoutCompat, isValidation: Boolean) {
       // val drawableRes = if (isValidation) R.drawable.spinner_validation else R.drawable.background_border_radius
        val strokeColor = if (isValidation) "#FF5733" else "#7494fb"

      //  val newBackground = ResourcesCompat.getDrawable(LayoutId.resources, drawableRes, null)
      //  LayoutId.background = newBackground

        val background = LayoutId.background
        if (background is GradientDrawable) {
            background.setStroke(4, Color.parseColor(strokeColor))
            background.cornerRadius = 16f
        }
    }

    fun updateLinearLayoutValidation(linearLayout: LinearLayout, isValidation: Boolean) {
      //  val drawableRes = if (isValidation) R.drawable.spinner_validation else R.drawable.background_border_radius
        val strokeColor = if (isValidation) "#FF5733" else "#7494fb"

       // val newBackground = ResourcesCompat.getDrawable(linearLayout.resources, drawableRes, null)
       // linearLayout.background = newBackground

        val background = linearLayout.background
        if (background is GradientDrawable) {
            background.setStroke(4, Color.parseColor(strokeColor))
            background.cornerRadius = 16f
        }
    }


    // Validation method to validate all fields and return true if all are filled
    fun validateTextInputFields(vararg fields: TextInputEditText): Boolean {
        for (field in fields) {
            if (field.text.isNullOrEmpty()) {
                field.requestFocus()  // Focus on the first empty field
                field.error = "This field is required"  // Show error message
                return false  // Return false as soon as we find an empty field
            }
        }
        return true
    }


    fun setupPhoneNumberValidation(
        editText: TextInputEditText,
        layout: TextInputLayout
    ) {
        val phoneNumberEditText =editText
        val phoneNumberLayout =layout

        // TextWatcher to listen to text changes and validate phone number
        phoneNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Check the length of the phone number
                val phoneNumber = s.toString()

                if (phoneNumber.length == 10) {
                    // If length is exactly 10, remove any error
                    phoneNumberLayout.isErrorEnabled = false
                } else {
                    // Show error if phone number is not 10 digits
                    phoneNumberLayout.isErrorEnabled = true
                    phoneNumberLayout.error = "Please enter exactly 10 digits"
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Optional: Limit input to only digits and 10 characters using InputFilter
        val filter = InputFilter { source, start, end, dest, dstart, dend ->
            // Allow only numeric characters
            if (source.isNotEmpty() && !source.matches("[0-9]*".toRegex())) {
                return@InputFilter ""  // Reject non-numeric characters
            }

            // Ensure length does not exceed 10 digits
            if (dest.length + source.length > 10) {
                return@InputFilter ""  // Reject if the total length exceeds 10 digits
            }

            null  // Accept the input
        }

        phoneNumberEditText.filters = arrayOf(filter)  // Apply the filter to the EditText
    }

    fun validation(
        editText1: EditText,
        editText2: EditText,
        editText3: EditText,
        editText4: EditText,
        editText5: EditText,
        editText6: EditText,
        editText7: EditText,
        editText8: EditText,
        editText9: EditText,
        editText10: EditText,
        editText11: EditText,
        editText12: EditText,
        editText13: EditText,
        editText14: EditText,
        editText15: EditText,
        editText16: EditText,
        editText17: EditText,
        editText18: EditText,
        editText19: EditText,
        editText20: EditText,
        editText21: EditText,
        editText22: EditText,
        editText23: EditText,
        editText24: EditText,
        editText25: EditText,
        editText26: EditText,
        editText27: EditText,
        editText28: EditText
    ): Boolean {
        return editText1.text.isNotEmpty() &&
                editText2.text.isNotEmpty() &&
                editText3.text.isNotEmpty() &&
                editText4.text.isNotEmpty() &&
                editText5.text.isNotEmpty() &&
                editText6.text.isNotEmpty() &&
                editText7.text.isNotEmpty() &&
                editText8.text.isNotEmpty() &&
                editText9.text.isNotEmpty() &&
                editText10.text.isNotEmpty() &&
                editText11.text.isNotEmpty() &&
                editText12.text.isNotEmpty() &&
                editText13.text.isNotEmpty() &&
                editText14.text.isNotEmpty() &&
                editText15.text.isNotEmpty() &&
                editText16.text.isNotEmpty() &&
                editText17.text.isNotEmpty() &&
                editText18.text.isNotEmpty() &&
                editText19.text.isNotEmpty() &&
                editText20.text.isNotEmpty() &&
                editText21.text.isNotEmpty() &&
                editText22.text.isNotEmpty() &&
                editText23.text.isNotEmpty() &&
                editText24.text.isNotEmpty() &&
                editText25.text.isNotEmpty() &&
                editText26.text.isNotEmpty() &&
                editText27.text.isNotEmpty() &&
                editText28.text.isNotEmpty()
    }




    fun validateGenderSelection(
        RadioGroup: RadioGroup,
        Massage: String,
        context: Context
    ): Boolean {
        if (RadioGroup.checkedRadioButtonId == -1) {
            Toast.makeText(context, Massage, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun SpinnerValidation(
        binding: Spinner,
        SpinnerLayout: LinearLayoutCompat,
        validation: validation,
        Massage: String,
        context: Context
    ):Boolean{
        if (binding.selectedItemPosition == 0) {
            validation.updateSpinnerValidation(SpinnerLayout, true)
            Toast.makeText(context, Massage, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

}