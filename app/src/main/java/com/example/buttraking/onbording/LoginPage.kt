package com.example.buttraking.onbording

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.buttraking.R
import com.example.buttraking.activity.MainActivity
import com.example.buttraking.api.RetrofitHelper
import com.example.buttraking.databinding.ActivityLoginPageBinding
import com.example.buttraking.helper.MethodLibrary
import com.example.buttraking.repository.UserRepository
import com.example.buttraking.viewmodel.LoginViewModel
import com.example.buttraking.viewmodelfactory.LoginViewModelFactory
import java.util.Locale

class LoginPage : AppCompatActivity() {

    private lateinit var binding: ActivityLoginPageBinding
    private lateinit var sharedPreferences: SharedPreferences
    val toolbox = MethodLibrary()
    var isTermsConditionChecked: Boolean = false
    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(UserRepository(RetrofitHelper.getApiService()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (toolbox.getDataBoolean("termsIschecked",this)) {
            binding.TermAndCondition.isChecked = true
        }

        toolbox.saveDataString("Hello", "Hello", this)
        sharedPreferences = getSharedPreferences("onboarding_prefs", MODE_PRIVATE)

        // Check if the user is logged in already
        if (sharedPreferences.getBoolean("is_logged_in", false)) {
            navigateToMainActivity()
            return
        }

        binding.txtTermsAndConditions.setOnClickListener {
            toolbox.showConfirmationDialog(
                context = this,
                title = "Terms and Condition",
                message = "Introduction: By using the software, users agree to follow the terms outlined. If they don't agree, they should not use it.\n" +
                        "\n" +
                        "Eligibility: The software is for use by educational institutions, teachers, students, and administrators. Users must be at least 18 years old and authorized to represent their institution.\n" +
                        "\n" +
                        "Account Registration: Users must create an account, ensuring that their login details are secure and up-to-date.\n" +
                        "\n" +
                        "License to Use: The software is provided under a limited, non-transferable license, and users cannot sublicense or transfer their rights.\n" +
                        "\n" +
                        "User Conduct: Users agree not to misuse the service, engage in unlawful activities, or attempt unauthorized access.\n" +
                        "\n" +
                        "Privacy and Data Protection: Users’ personal data will be handled according to the company's privacy policy.\n" +
                        "\n" +
                        "Fees and Payment: If applicable, users are responsible for paying fees as outlined in pricing terms. Unpaid fees may result in account suspension.\n" +
                        "\n" +
                        "Intellectual Property: The software and all related intellectual property are owned by the company. Users cannot copy or alter it without permission.\n" +
                        "\n" +
                        "Service Availability and Maintenance: The software may experience downtime due to updates or maintenance.\n" +
                        "\n" +
                        "Termination: Either party can terminate the account at any time. Users must stop using the service upon termination.\n" +
                        "\n" +
                        "Limitation of Liability: The company is not liable for indirect or consequential damages. Liability is limited to what was paid for the service.\n" +
                        "\n" +
                        "Indemnification: Users agree to protect the company from any claims, damages, or losses resulting from their use of the software.\n" +
                        "\n" +
                        "Changes to Terms: The company can update the terms and will notify users of significant changes.\n" +
                        "\n" +
                        "Governing Law: The terms are governed by the laws of the company’s jurisdiction.\n" +
                        "\n" +
                        "Contact Information: Users can contact the company for any queries about the terms.",
                positiveButtonText = "ok",
                positiveButtonAction = {  },
                negativeButtonText = "",
                negativeButtonAction = {  },
                cancelable = true)
        }

        // Set listeners for login and password visibility toggle

        setListeners()
        observeViewModel()

        binding.eyeOpen.setOnClickListener {
            togglePasswordVisibility(true)
        }

        binding.eyeClose.setOnClickListener {
            togglePasswordVisibility(false)
        }
        setKeyboardListener(findViewById(android.R.id.content),
            onKeyboardOpen = {
                MethodLibrary().LayoutMarginTopAnimate(binding.LoginLayout, 180, 250, this)

            },
            onKeyboardClose = {
                MethodLibrary().LayoutMarginTopAnimate(binding.LoginLayout, 300, 250, this)
            })
    }

    private fun setListeners() {
        binding.btnLogin.setOnClickListener {
            val schoolId = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (schoolId.isNotEmpty() && password.isNotEmpty()) {
                if (binding.TermAndCondition.isChecked){
                    toolbox.saveDataBoolean("termsIschecked", true, this)
                    loginViewModel.login(schoolId, password)
                }else{
                    toolbox.showConfirmationDialog(
                        context = this,
                        title = "Terms and Conditions",
                        message = "Please read and accept the terms and conditions by checking the box.",
                        positiveButtonText = "ok",
                        positiveButtonAction = {  },
                        negativeButtonText = "",
                        negativeButtonAction = {  },
                        cancelable = true)
                }

            } else {
                Toast.makeText(this, "Please enter both school ID and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel() {
        loginViewModel.loginResponse.observe(this) { response ->

            if (response?.status==false) {
                Toast.makeText(this, "Your account is inactive. Please contact your school admin.", Toast.LENGTH_LONG).show()
                return@observe
            }

            if (response?.status == true) {

                Toast.makeText(this, "Login successful ${response.role}", Toast.LENGTH_SHORT).show()

                val editor = sharedPreferences.edit()

                when (response.role.lowercase(Locale.ROOT)) {
                    "admin" -> {
                        editor.putString("school_id", response.school_id)
                            .putString("school_name", response.school_name)
                            .putString("password", response.password)
                            .putString("number", response.phone_number)
                            .putString("created_at", response.updated_at)
                            .putString("role", response.role)
                            .putString("institute_address", response.institute_address)
                            .putString("website", response.website)
                            .putString("tagline", response.tag_line)
                        rememberMe()

                    }
                    "Teacher", "teacher" -> {
                        editor.putString("school_id", response.school_id)
                            .putString("employee_name", response.employee_name)
                            .putString("institute_logo", response.institute_logo)
                            .putString("date_of_joining", response.date_of_joining)
                            .putString("status", response.status.toString())
                            .putString("school_name", response.school_name)
                            .putString("employee_id", response.employee_id)
                            .putString("role", response.role)
                            .putString("number", response.number)
                            .putString("f_h_name", response.f_h_name)
                            .putString("national_id", response.national_id)
                            .putString("education", response.education)
                            .putString("gender", response.gender)
                            .putString("religion", response.religion)
                            .putString("blood_group", response.blood_group)
                            .putString("home_address", response.home_address)
                            .putString("email", response.email)
                            .putString("username", response.username)
                            .putString("picture", response.picture)
                            .putString("password", response.password)
                        rememberMe()
                        // Toast.makeText(this,  response.f_h_name, Toast.LENGTH_SHORT).show()
                    }

                    "principal", "Principal" ->{
                        editor.putString("school_id", response.school_id)
                            .putString("employee_name", response.employee_name)
                            .putString("institute_logo", response.institute_logo)
                            .putString("date_of_joining", response.date_of_joining)
                            .putString("school_name", response.employee_id)
                            .putString("employee_id", response.employee_id)
                            .putString("status", response.status.toString())
                            .putString("school_name", response.school_name)
                            .putString("role", response.role)
                            .putString("number", response.number)
                            .putString("f_h_name", response.f_h_name)
                            .putString("national_id", response.national_id)
                            .putString("education", response.education)
                            .putString("gender", response.gender)
                            .putString("religion", response.religion)
                            .putString("blood_group", response.blood_group)
                            .putString("home_address", response.home_address)
                            .putString("email", response.email)
                            .putString("username", response.username)
                            .putString("picture", response.picture)
                            .putString("password", response.password)
                        rememberMe()
                    }
                    "Driver","driver" -> { // Ensure it's lowercase
                        editor.putString("school_id", response.school_id)
                            .putString("driver_name", response.driver_name) // Correct key
                            .putString("role", response.role)
                            .putString("number", response.number)
                            .putString("username", response.username)
                            .putString("password", response.password)
                            .putString("id", response.id)

                        rememberMe()
                    }



                    "student" ->{
                        editor.putString("st_school_id", response.school_id)
                            .putString("role", response.role)
                            .putString("st_name", response.st_name)
                            .putString("institute_logo", response.institute_logo)
                            .putString("student_id", response.student_id)
                            .putString("picture", response.picture)
                            .putString("school_name", response.school_name)
                            .putString("registration_no", response.registration_no)
                            .putString("school_id", response.school_id)
                            .putString("st_class", response.st_class)
                            .putString("dt_of_admission", response.dt_of_admission)
                            .putString("username", response.username)
                            .putString("password", response.password)
                            .putString("st_status", response.st_status)
                            .putString("dt_of_birth", response.dt_of_birth)
                            .putString("number", response.number)
                            .putString("religion", response.religion)
                            .putString("cast", response.cast)
                            .putString("gender", response.gender)
                            .putString("blood_group", response.blood_group)
                            .putString("disease_if_any", response.disease_if_any)
                            .putString("discount_fee", response.discount_fee)
                            .putString("family", response.family)
                            .putString("siblings", response.siblings)
                            .putString("orphan_student", response.orphan_student)
                            .putString("osc", response.osc)
                            .putString("st_previous_school", response.st_previous_school)
                            .putString("previous_id", response.previous_id)
                            .putString("identification_mark", response.identification_mark)
                            .putString("address", response.address)
                            .putString("father_name", response.father_name)
                            .putString("father_id", response.father_id)
                            .putString("father_education", response.father_education)
                            .putString("father_mobile", response.father_mobile)
                            .putString("father_profession", response.father_profession)
                            .putString("father_occupation", response.father_occupation)
                            .putString("father_income", response.father_income)
                            .putString("mother_name", response.mother_name)
                            .putString("mother_mobile", response.mother_mobile)
                            .putString("mother_id", response.mother_id)
                            .putString("mother_education", response.mother_education)
                            .putString("mother_profession", response.mother_profession)
                            .putString("mother_occupation", response.mother_occupation)
                            .putString("mother_income", response.mother_income)
                        rememberMe()

                    }
                    else -> {
                        Toast.makeText(this, "Unknown role", Toast.LENGTH_SHORT).show()
                        return@observe
                    }
                }
                editor.apply()
                navigateToMainActivity()
            } else {

                Toast.makeText(this, "Invalid login credentials", Toast.LENGTH_SHORT).show()
            }
        }
        var count = 0
        loginViewModel.errorMessage.observe(this) { error ->
            if (!toolbox.isInternetAvailable(this)){
                toolbox.deviceOffLineMassage(this)
            }else{
                count++
                if (count == 5) {
                    toolbox.showConfirmationDialog(
                        context = this,
                        title = "Invalid Credentials",
                        message = "Please contact your school.",
                        positiveButtonText = "OKey",
                        positiveButtonAction = { count = 0 },
                        negativeButtonText = "",
                        negativeButtonAction = {},
                        cancelable = false
                    )

                }

                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
                MethodLibrary().vibrate(this ,50, 10)
            }

//            val intent: Intent = Intent(Settings.ACTION_DATA_ROAMING_SETTINGS)
//            startActivity(Intent(Settings.ACTION_DATA_ROAMING_SETTINGS))
        }
    }

    private fun togglePasswordVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.eyeOpen.visibility = View.GONE
            binding.eyeClose.visibility = View.VISIBLE
            binding.passwordEditText.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            binding.eyeClose.visibility = View.GONE
            binding.eyeOpen.visibility = View.VISIBLE
            binding.passwordEditText.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        binding.passwordEditText.setSelection(binding.passwordEditText.text.length)
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    private fun rememberMe() {
        if (binding.rememberMeCheckBox.isChecked){
            val editor = sharedPreferences.edit()
            editor.putBoolean("onboarding_completed", true)
                .putBoolean("is_logged_in", true)
            editor.apply()
        }
    }

    fun setKeyboardListener(view: View, onKeyboardOpen: () -> Unit, onKeyboardClose: () -> Unit) {
        var isKeyboardOpen = false

        view.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                val rect = Rect()
                view.getWindowVisibleDisplayFrame(rect)
                val screenHeight = view.height
                val keypadHeight = screenHeight - rect.bottom

                // Check if the keyboard is open (based on the height difference)
                val isCurrentlyOpen = keypadHeight > screenHeight * 0.15

                if (isCurrentlyOpen && !isKeyboardOpen) {
                    // Keyboard has just opened
                    isKeyboardOpen = true
                    onKeyboardOpen() // Call your method when the keyboard opens
                } else if (!isCurrentlyOpen && isKeyboardOpen) {
                    // Keyboard has just closed
                    isKeyboardOpen = false
                    onKeyboardClose() // Call your method when the keyboard closes
                }

                return true
            }
        })
    }

}
