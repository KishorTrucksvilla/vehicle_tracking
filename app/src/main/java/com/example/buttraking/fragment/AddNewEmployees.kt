package com.example.buttraking.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.buttraking.IDDetail.SchoolId
import com.example.buttraking.R
import com.example.buttraking.databinding.FragmentAddNewEmployeesBinding
import com.example.buttraking.helper.MethodLibrary
import com.example.buttraking.viewmodel.AddNewEmployeesViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddNewEmployees : Fragment() {

    private lateinit var buttonUpload: Button
    private val GALLERY_REQUEST_CODE = 100
    private val CAMERA_REQUEST_CODE = 101
    private var imageUri: Uri? = null
    private lateinit var imageView: ImageView
    private var selectedImageUri: Uri? = null
    private val PERMISSION_REQUEST_CODE = 103

    companion object {
        const val REQUEST_IMAGE_CAPTURE_1: Int = 1
        const val REQUEST_IMAGE_GALLERY_1: Int = 11
    }

    var photo1: Bitmap? = null

    private val REQUEST_IMAGE_CAPTURE = 1
    private var currentPhotoPath: String? = null
    private lateinit var binding: FragmentAddNewEmployeesBinding
    private lateinit var viewModel: AddNewEmployeesViewModel
    //private lateinit var selectedReligion: String
    private val calendar = Calendar.getInstance()
    val toolBox = MethodLibrary()
    private lateinit var mProgress: ProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddNewEmployeesBinding.inflate(inflater, container, false)

        val sharedPreferences = requireActivity().getSharedPreferences(
            "onboarding_prefs",
            AppCompatActivity.MODE_PRIVATE
        )
        val schoolId = sharedPreferences.getString("school_id", null)

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireContext() as Activity,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        }

        if (schoolId != null) {
            Log.d("AddNewEmployees", "School ID retrieved from SharedPreferences: $schoolId")
        } else {
            Log.d("AddNewEmployees", "School ID not found in SharedPreferences")
        }



        imageView = binding.imageEmp

        setupListeners()
        setupDatePickers()
        mProgress = createProgressDialog()




        setupMobileNumberValidation()

        return binding.root


    }

    private fun createProgressDialog(): ProgressDialog {
        return ProgressDialog(requireContext()).apply {
            setMessage("Please wait...")
            setCancelable(false)
        }
    }



    /* private fun initObservers() {
         viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
             if (!toolBox.isInternetAvailable(requireContext())) {
                 mProgress.dismiss()
                     toolBox.deviceOffLineMassage(requireContext())
             } else {
                 mProgress.dismiss()
                 showPreviousDataDialog()

             }
         }
     }*/

    private fun setupListeners() {
        binding.btnSubmit.setOnClickListener {
            mProgress.show()
            if (!toolBox.isInternetAvailable(requireContext())) {
                mProgress.dismiss()
                toolBox.deviceOffLineMassage(requireContext()) // Show message when offline
                return@setOnClickListener
            }
            mProgress.show()
            if (validateEmployeeData()) {
                mProgress.show()
                val attachmentBase64 = photo1?.let {
                  //  ImageUtil.bitmapToBase64(it) // Convert Bitmap to Base64 if photo1 is available
                } //?:// ImageUtil.getBase64StringFromImageView(binding.imageEmp) // Fallback to ImageView if photo1 is null

                val mobileNumber = binding.edtMobileNumber.text.toString().trim()

                // Check length of the strings before calling substring
                val password =
                    if (binding.etEmployeesName.text.toString().length >= 3 && mobileNumber.length >= 7) {
                        binding.etEmployeesName.text.toString().trim()
                            .substring(0, 3) + mobileNumber.substring(6)
                    } else {
                        "defaultPassword"
                    }

                val employeeData = mapOf(
                    "employee_name" to binding.etEmployeesName.text.toString().trim(),
                    "number" to mobileNumber,
                    "employee_role" to binding.edtemployeesroll.selectedItem.toString(),
                    "picture" to attachmentBase64,
                    "date_of_joining" to binding.edtdateofjoining.text.toString().trim(),
                    "monthly_salary" to binding.edtMonthlysalary.text.toString().trim(),
                    "f_h_name" to binding.edtfatherhasband.text.toString().trim(),
                    "education" to binding.edtedacation.text.toString().trim(),
                    "gender" to binding.edtgender.selectedItem.toString(),
                    "religion" to binding.edtreligion.selectedItem.toString(),
                    "blood_group" to binding.edtbloodgroup.selectedItem.toString(),
                    "experience" to binding.edtexperience.text.toString().trim(),
                    "date_of_birth" to binding.edtdateofbirth.text.toString().trim(),
                    "home_address" to binding.edthomeaddress.text.toString().trim(),
                    "email" to binding.edtemailaddres.text.toString().trim(),
                    "national_id" to binding.edtnationalId.text.toString().trim(),
                    "username" to mobileNumber,
                    "password" to password,
                    "status" to "Active",
                    "school_id" to SchoolId().getSchoolId(requireContext())
                )

                Log.d("AddEmployeeRequest", "Request Data: $attachmentBase64")
                viewModel.addNewEmployee(employeeData)
            }

            viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                if (isLoading) {
                    mProgress.show()
                } else {
                    mProgress.dismiss()
                }
            }

            viewModel.isLoading.observe(viewLifecycleOwner) { response ->
                mProgress.dismiss()
                if (response.not()) {
                    showPreviousDataDialog()
                } else {
                    /*Toast.makeText(
                        requireContext(),
                        "Failed to add employee: ${response.errorMessage}",
                        Toast.LENGTH_SHORT
                    ).show()*/
                }
            }
        }
        binding.edtChooseFile.setOnClickListener {
            binding.imageEmp.setImageDrawable(null)
            showImagePickerDialog(REQUEST_IMAGE_CAPTURE_1, REQUEST_IMAGE_GALLERY_1)
        }
    }

    private fun showPreviousDataDialog() {

        toolBox.showConfirmationDialog(
            context = requireContext(),
            title = "Employee Added Successfully",
            message = "Would you like to add more employee?",
            positiveButtonText = "YES",
            positiveButtonAction = { toolBox.fragmentChanger(AddNewEmployees(), requireContext()) },
            negativeButtonText = "NO",
            negativeButtonAction = { toolBox.fragmentChanger(AllEmployees(), requireContext()) },
            cancelable = false)
    }

    private fun setupMobileNumberValidation() {
        binding.edtMobileNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable?) {
                val mobileNumber = editable.toString().trim()

                if (mobileNumber.length == 10 && mobileNumber.matches(Regex("\\d{10}"))) {
                    // Change the border color of the TextInputLayout to green when valid
                    binding.textInputLayoutMobileNumber.setBoxStrokeColor(
                        ContextCompat.getColor(
                            context!!, R.color.green
                        )
                    )
                } else {
                    // Reset the border color if the number is invalid
                    binding.textInputLayoutMobileNumber.setBoxStrokeColor(
                        ContextCompat.getColor(
                            context!!, R.color.red
                        )
                    )
                }
            }
        })
    }

    private fun validateEmployeeData(): Boolean {
        mProgress.dismiss()

        val name = binding.etEmployeesName.text.toString().trim()
        val mobileNumber = binding.edtMobileNumber.text.toString().trim()
        val role = binding.edtemployeesroll.selectedItemPosition
        val dateOfJoining = binding.edtdateofjoining.text.toString().trim()
        val salary = binding.edtMonthlysalary.text.toString().trim()
        val fHName = binding.edtfatherhasband.text.toString().trim()
        val education = binding.edtedacation.text.toString().trim()
        val gender = binding.edtgender.selectedItemPosition
        val dob = binding.edtdateofbirth.text.toString().trim()
        val address = binding.edthomeaddress.text.toString().trim()
        val nationalId = binding.edtnationalId.text.toString().trim()
        val religion = binding.edtreligion.selectedItemPosition
        val emailaddress = binding.edtemailaddres.text.toString().trim()

        return when {
            name.isEmpty() -> {
                showError(binding.etEmployeesName, "Employee Name is required")
                false
            }
            name.length < 3 || !name.matches(Regex("^[A-Za-z ]{3,}$")) -> {
                showError(binding.etEmployeesName, "Name must contain at least three alphabetic characters")
                false
            }

            mobileNumber.isEmpty() || !mobileNumber.matches(Regex("\\d{10}")) -> {
                showError(binding.edtMobileNumber, "Enter a valid 10-digit Mobile Number")
                false
            }
            role == 0 -> { // Assuming 0 is the default "Select Role" position
                showToast("Select a valid Employee Role")
                false
            }
            dateOfJoining.isEmpty() -> {
                showError(binding.edtdateofjoining, "Enter Date of Joining")
                false
            }
            salary.isEmpty() -> {
                showError(binding.edtMonthlysalary, "Enter Monthly Salary")
                false
            }
            fHName.isEmpty() -> {
                showError(binding.edtfatherhasband, "Enter Father's/Husband's Name")
                false
            }
            education.isEmpty() -> {
                showError(binding.edtedacation, "Enter Education")
                false
            }
            gender == 0 -> { // Assuming 0 is the default "Select Gender" position
                showToast("Select a valid Gender")
                false
            }
            dob.isEmpty() -> {
                showError(binding.edtdateofbirth, "Enter Date of Birth")
                false
            }
            address.isEmpty() -> {
                showError(binding.edthomeaddress, "Enter Home Address")
                false
            }
            nationalId.isEmpty() -> {
                showError(binding.edtnationalId, "Enter Aadhaar Id")
                false
            }
            religion == 0 -> { // Assuming 0 is the default "Select Religion" position
                showToast("Select a valid Religion")
                false
            }
            emailaddress.isEmpty() -> {
                showError(binding.edtemailaddres, "Enter Email Address")
                false
            }
            else -> true
        }
    }

    private fun showError(view: EditText, message: String) {
        view.error = message
        view.requestFocus()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setupDatePickers() {
        binding.edtdateofjoining.setOnClickListener {
            showDatePickerDialog { date -> binding.edtdateofjoining.setText(date) }
        }

        binding.edtdateofbirth.setOnClickListener {
            showDatePickerDialog { date -> binding.edtdateofbirth.setText(date) }
        }
    }

    private fun showDatePickerDialog(onDateSet: (String) -> Unit) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                .format(calendar.apply {
                    set(Calendar.YEAR, selectedYear)
                    set(Calendar.MONTH, selectedMonth)
                    set(Calendar.DAY_OF_MONTH, selectedDay)
                }.time)

            onDateSet(formattedDate)
        }, year, month, day).show()
    }

    private fun showImagePickerDialog(requestImageCapture: Int, requestImageGallery: Int) {
        val options = arrayOf("Camera", "Choose from Gallery")
        AlertDialog.Builder(requireContext())
            .setTitle("Pick an Image")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> captureImage(requestImageCapture)  // Capture image from camera
                    1 -> openGallery(requestImageGallery)   // Choose image from gallery
                }
            }
            .show()
    }




//    private fun createImageFile(): File {
//        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
//        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
//            currentPhotoPath = absolutePath
//        }
//    }

    private fun captureImage(IMAGE_CAPTURE_CODE: Int) {
        try {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, IMAGE_CAPTURE_CODE)
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFileName = "JPEG_$timeStamp"
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        // Create a temporary image file
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",         /* suffix */
            storageDir      /* directory */
        ).apply {
            // Save the file path to use later
            currentPhotoPath = absolutePath
        }
        return image
    }


/*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            when (requestCode) {
                SendMessageAdmin.REQUEST_IMAGE_CAPTURE_1 -> {
                    val photo = data?.extras?.get("data") as? Bitmap
                    photo?.let {
                        Log.d("onActivityResult", "Captured Image Bitmap: $it")
                        photo1 = it // Save the captured photo for later Base64 conversion
                        binding.imageEmp.setImageBitmap(it)
                    } ?: run {
                        binding.imageEmp.setImageResource(R.drawable.imagenotfound) // Placeholder
                    }
                }

                SendMessageAdmin.REQUEST_IMAGE_GALLERY_1 -> {
                    data?.data?.let { selectedImageUri ->
                        try {
                            val photo = MediaStore.Images.Media.getBitmap(
                                requireContext().contentResolver,
                                selectedImageUri
                            )
                            Log.d("onActivityResult", "Selected Image Bitmap: $photo")
                            photo1 = photo // Save the selected photo for later Base64 conversion
                            binding.imageEmp.setImageBitmap(photo)
                        } catch (e: IOException) {
                            e.printStackTrace()
                            binding.imageEmp.setImageResource(R.drawable.imagenotfound) // Placeholder
                        }
                    } ?: run {
                        binding.imageEmp.setImageResource(R.drawable.imagenotfound) // Placeholder
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            binding.imageEmp.setImageResource(R.drawable.imagenotfound) // Placeholder
        }
    }
*/

    // Handle configuration changes or app restart
    override fun onResume() {
        super.onResume()

        if (photo1 == null) {
            binding.imageEmp.setImageResource(R.drawable.imagenotfound) // Placeholder
        } else {
            binding.imageEmp.setImageBitmap(photo1)
        }
    }


    private fun getRealPathFromURI(uri: Uri): String {
        var result: String? = null
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result ?: uri.path.toString()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE &&
            grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
           // captureImage(com.example.schoolerp.Fragments.SendMessageAdmin.REQUEST_IMAGE_CAPTURE_1)
        } else {
            Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

   /* private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }


    private fun convertImageToBase64(uri: Uri): String {
        val bitmap = binding.imageEmp.drawable.toBitmap()
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 10, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }*/

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    private fun openGallery(requestGallery: Int) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, requestGallery)
    }
}