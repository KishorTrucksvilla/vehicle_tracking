package com.example.buttraking.fragment

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.buttraking.R
import com.example.buttraking.databinding.FragmentDriverDetailBinding
import com.example.buttraking.dataclass.VehicleDataAdmin
import com.example.buttraking.helper.MethodLibrary

class DriverDetail : Fragment() {
    private lateinit var binding: FragmentDriverDetailBinding
    var toolbox= MethodLibrary()
    private var isExpand = true
    val toolBox = MethodLibrary()
    private var EmployeeImage:String="assetsNew/img/student/"
   // var  teacherDetails = TeacherDetails()
    private lateinit var empPass: String
    private lateinit var profilePic :  String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentDriverDetailBinding.bind(inflater.inflate(R.layout.fragment_driver_detail, null))
        arguments?.getParcelable<VehicleDataAdmin>("driver")?.let { emp ->
            populateFields(emp)
        }
        return binding.root
    }

    private fun populateFields(emp: VehicleDataAdmin){
        binding.etStudentName.setText(emp.driver_name)
        //  binding.etregistrationNo.setText(emp.re)
        binding.edtrole.setText(emp.driver_type)
        binding.etUsername.setText(emp.username)
        binding.etPassword.setText(emp.password)
        // binding.etStatus.setText(emp.st_status)
        binding.etPhoneNumber.setText(emp.number)

        //  MethodLibrary().displayImage("${ImageUtil.BASE_URL_IMAGE}employee/${emp.picture}", binding.imgEmp , binding.root.context)

        empPass = emp.password



        var isPasswordVisible = false

        binding.etPassword.text = "*".repeat(empPass.length)

        binding.togglePasswordVisibility.setOnClickListener {
            isPasswordVisible = !isPasswordVisible

            if (isPasswordVisible) {

                val keyguardManager = requireContext().getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

                if (keyguardManager.isKeyguardSecure) {
                    val intent = keyguardManager.createConfirmDeviceCredentialIntent(
                        "Authenticate",
                        "Please enter your device password to view the password"
                    )
                    startActivityForResult(intent, REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS)
                }else{
                    binding.etPassword.text = empPass
                    binding.togglePasswordVisibility.setImageResource(R.drawable.eyeoffpassword)
                }
            } else {
                // Hide the password
                binding.etPassword.text = "*".repeat(empPass.length)
                binding.togglePasswordVisibility.setImageResource(R.drawable.eyeonpassword) // Change to "eye-closed" icon
            }

        }

        binding.txtSeeMore.setOnClickListener{
            isExpand = !isExpand
            MethodLibrary().toggleLayoutExpansion(
                AnimationLayout = binding.layoutEmpInfo,
                PrimaryText = binding.txtSeeMore,
                PrimaryTextMessage ="See More",
                SecondText = binding.txtSeeMore,
                SecondTextMessage = "See less",
                startingHeight = 0,
                EndingHeight = 380,
                Timing = 500L,
                DefaultValue = isExpand,
                requireContext()
            )
        }



        /*  binding.txtSeeMore.setOnClickListener {
              binding.layoutStInfo.visibility =
                  if (binding.layoutStInfo.visibility == View.GONE) {
                      binding.txtSeeMore.text = "See Less"
                      View.VISIBLE

                  } else {
                      binding.txtSeeMore.text
                      View.GONE
                  }*/
    }
/*
    private fun printSalarySlip() {
        val printManager = requireContext().getSystemService(Context.PRINT_SERVICE) as PrintManager
        val empdata = arguments?.getParcelable<AllEmployee>("employee")
        val printAdapter = SearchEmpPrintAdapter(requireContext(), empdata)
        printManager.print("Employees Information", printAdapter, PrintAttributes.Builder().build())
    }
*/


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS) {
            if (resultCode == Activity.RESULT_OK) {
                binding.etPassword.text = empPass
                binding.togglePasswordVisibility.setImageResource(R.drawable.eyeoffpassword)

            } else {
                // Authentication failed or canceled, keep the password hidden
                binding.etPassword.text = "*".repeat(empPass.length)
                binding.togglePasswordVisibility.setImageResource(R.drawable.eyeonpassword)

                Toast.makeText(context, "Authentication failed or canceled.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
    companion object {
        const val REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS = 12345
    }

}




