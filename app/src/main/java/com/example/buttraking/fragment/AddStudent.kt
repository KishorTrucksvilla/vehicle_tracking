package com.example.buttraking.fragment

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.buttraking.IDDetail.SchoolId
import com.example.buttraking.R
import com.example.buttraking.activity.MainActivity
import com.example.buttraking.api.RetrofitHelper
import com.example.buttraking.databinding.FragmentAddStudentBinding
import com.example.buttraking.dataclass.Student
import com.example.buttraking.helper.MethodLibrary
import com.example.buttraking.repository.AddStudentRepository
import com.example.buttraking.repository.AllClassRepository
import com.example.buttraking.repository.getDriverdataRepository
import com.example.buttraking.viewmodel.AddStudentViewModel
import com.example.buttraking.viewmodel.AllClassViewModel
import com.example.buttraking.viewmodel.getDriverdataViewModel
import com.example.buttraking.viewmodelfactory.AddStudentViewModelFactory
import com.example.buttraking.viewmodelfactory.AllClassViewModelFactory
import com.example.buttraking.viewmodelfactory.getdriverdataViewModelFactory
import java.io.IOException
import com.example.buttraking.helper.validation

class AddStudent : Fragment() {
    val toolBox = MethodLibrary()
    val validation = validation()

    private lateinit var binding: FragmentAddStudentBinding
    private var selectedGender: String? = null
    private var selectedDisability: String? = null
    private var selectedOrphan: String? = null
    private lateinit var edDateOfBirth: EditText
    private lateinit var edDateOfAdmission: EditText
    private lateinit var viewModelAllClass: AllClassViewModel

    private lateinit var imageView: ImageView
    private var selectedImageUri: Uri? = null

    private val CAMERA_REQUEST_CODE = 100
    private val GALLERY_REQUEST_CODE = 200

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        const val PICK_IMAGE_REQUEST = 2
    }



    // Initialize ViewModel using a factory
    private lateinit var viewModel: AddStudentViewModel
    private lateinit var viewmodledriverdata: getDriverdataViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddStudentBinding.inflate(inflater, container, false)
        Setaddviewmodle()

        if (!toolBox.isInternetAvailable(requireContext())){
            toolBox.deviceOffLineMassage(AddStudent(),requireContext())
        }else{
            edDateOfBirth = binding.edDateOfBirth
            edDateOfAdmission = binding.etDateOfAdmission
            imageView = binding.imageStudent

            validation.setupPhoneNumberValidation(binding.etPhoneNumber, binding.etPhoneNumberLayout)
            validation.setupPhoneNumberValidation(binding.etMotherMobile, binding.etMotherMobileLayout)
            validation.setupPhoneNumberValidation(binding.etFatherMobile, binding.etFatherMobileLayout)

            toolBox.clicked(edDateOfBirth){toolBox.DatePicker(edDateOfBirth,requireContext())}
            toolBox.clicked(edDateOfAdmission){toolBox.DatePicker(edDateOfAdmission,requireContext())}

            binding.btnUpload.setOnClickListener { openGallery() }

            binding.btnSubmit.setOnClickListener {
                if (!toolBox.isInternetAvailable(requireContext())){
                    toolBox.deviceOffLineMassage(requireContext())
                }else{
                    handleGenderSelection()
                    handleOrphanSelection()
                    if (Validation()){
                        if(binding.etStudentName.text.toString().length>=3){
                            if (binding.etPhoneNumber.text.toString().length == 10){
                                handleDisablitySelection()
                                addStudent()
                            }
                        }else{
                            Toast.makeText(requireContext(), "Please enter valid name", Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }

            observeViewModel()
            arguments?.getParcelable<Student>("student")?.let { student ->
                populateFields(student)
            }
            (SchoolId().getSchoolId(requireContext()))
            setupAllClassObserver()

            binding.btnResetButton.setOnClickListener(){
                toolBox.showConfirmationDialog(
                    context = requireContext(),
                    title = "Reset Data!",
                    message = "Are you sure you want to clear all data.",
                    positiveButtonText = "Yes",
                    positiveButtonAction = { toolBox.fragmentChanger(AddStudent(), requireContext()) },
                    negativeButtonText = "no",
                    negativeButtonAction = { },
                    cancelable = true )
            }
        }

        setupdriverviewmodel()
        setupDriverSpinner()

        return binding.root
    }
    private fun Setaddviewmodle(){
        val apiService=RetrofitHelper.getApiService()
        val repository = AddStudentRepository(apiService)
        val factory = AddStudentViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(AddStudentViewModel::class.java)
    }
    private fun setupdriverviewmodel() {
        val apiService = RetrofitHelper.getApiService()
        val repository = getDriverdataRepository(apiService)
        val factory = getdriverdataViewModelFactory(repository)
        viewmodledriverdata = ViewModelProvider(this, factory).get(getDriverdataViewModel::class.java)

        viewmodledriverdata.fetchDriverdata(SchoolId().getSchoolId(requireContext()))
    }

    private fun setupDriverSpinner() {
        // Observe driver data from ViewModel
        viewmodledriverdata.driverdata.observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                val driverList = it.data // Assuming `driverData` is the list inside response

                if (driverList.isNotEmpty()) {
                    val driverNames = mutableListOf("Select Driver")
                    val driverIds = mutableListOf("Select Driver ID")

                    driverNames.addAll(driverList.map { driver -> driver.driver_name ?: "Unknown" })
                    driverIds.addAll(driverList.map { driver -> driver.id })

                    // Adapter for Driver Names
                    val driverAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, driverNames)
                    driverAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.sppnerdriver.adapter = driverAdapter

                    // Adapter for Driver IDs
                    val driverIDAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, driverIds)
                    driverIDAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.onlyId.adapter = driverIDAdapter

                    // Handle Driver Selection
                    binding.sppnerdriver.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                            if (position > 0) { // Ignore "Select Driver"
                                binding.onlyId.setSelection(position)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {}
                    }
                } else {
                    Toast.makeText(requireContext(), "No drivers found", Toast.LENGTH_SHORT).show()
                }
            }
        })

        // Fetch drivers
    }

    private fun populateFields(student: Student) {
        // Get the RadioButton that is selected
        val radioButton = binding.etOrphanStudentRadio.findViewById<RadioButton>(binding.etOrphanStudentRadio.checkedRadioButtonId)
        val selectedId: Int = binding.etOrphanStudentRadio.checkedRadioButtonId
        val selectedRadioButton: RadioButton = binding.root.findViewById(selectedId)
        Toast.makeText(requireContext(), selectedRadioButton.text.toString(), Toast.LENGTH_SHORT).show()

        binding.etschoolId.setText(student.school_id)
        binding.etStudentName.setText(student.st_name)
        binding.etregistrationNo.setText(student.registration_no)
        binding.spStudentSelectClass.setSelection(getSpinnerIndex(binding.spStudentSelectClass, student.st_class))
        binding.etDateOfAdmission.setText(student.dt_of_admission)
        binding.etDiscountFees.setText(student.discount_fee)
        binding.etPhoneNumber.setText(student.number)
        binding.edDateOfBirth.setText(student.dt_of_birth)
        binding.spSelectReligion.setSelection(getSpinnerIndex(binding.spSelectReligion, student.religion))
        binding.spSelectBloodGroup.setSelection(getSpinnerIndex(binding.spSelectBloodGroup, student.blood_group))
        binding.etPreviousId.setText(student.previous_id)
        binding.etSelectFamily.setText(student.family)
        binding.edDiseaseIfAny.setText(student.disease_if_any)
        binding.etAnyAdditionalNote.setText(student.additional_note)
        binding.etTotalSibling.setText(student.siblings)
        binding.etAddress.setText(student.address)
        binding.etFatherName.setText(student.father_name)
        binding.etFatherID.setText(student.father_id)
        binding.etFatherEducatino.setText(student.father_education)
        binding.etFatherOccupation.setText(student.father_occupation)
        binding.etMotherName.setText(student.mother_name)
        binding.etMotherID.setText(student.mother_id)
        binding.etIdentityMark.setText(student.identification_mark)
        binding.etPreviousSchool.setText(student.st_previous_school)
        binding.etcreatedAt.setText(student.created_at)

        // Handle setting gender (assuming radio buttons or dropdown)
        setSelectedGender(student.gender)
        setSelectedDisablity(student.osc)
        setSelectedOrphan(student.orphan_student)

        // Load the image if available
        if (student.picture.isNotEmpty()) {
            val imageUri = Uri.parse(student.picture)
            binding.imageStudent.setImageURI(imageUri)  // Assuming `imageView` is the ImageView for the picture
        }
    }

    // Helper function to get spinner index
    private fun getSpinnerIndex(spinner: Spinner, value: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString().equals(value, ignoreCase = true)) {
                return i
            }
        }
        return 0
    }

    // Assuming you have a function to set the selected gender (for RadioButtons or Spinner)
    private fun setSelectedGender(gender: String) {
        when (gender) {
            "Male" -> binding.radioMale.isChecked = true
            "Female" -> binding.radioFemale.isChecked = true
        }
    }

    private fun setSelectedDisablity(disablity: String) {
        when (disablity) {
            "Yes" -> binding.disableYes.isChecked = true
            "No" -> binding.disableNo.isChecked = true
        }
    }

    private fun setSelectedOrphan(disablity: String) {
        when (disablity) {
            "Yes" -> binding.orphanYes.isChecked = true
            "No" -> binding.orphanNo.isChecked = true
        }
    }

    private fun handleGenderSelection() {
        val selectedId: Int = binding.radioGroupGender.checkedRadioButtonId
        if (selectedId != -1) {
            val selectedRadioButton: RadioButton = binding.root.findViewById(selectedId)
            selectedGender = selectedRadioButton.text.toString()
//            Toast.makeText(activity, "Selected: $selectedGender", Toast.LENGTH_SHORT).show()
        } else {
//            Toast.makeText(activity, "No gender selected", Toast.LENGTH_SHORT).show()
        }
    }



    private fun getSelectedGender(): String {
        return selectedGender ?: "Gender Not Selected"
    }
    private fun getSelectedDisablity(): String {
        return selectedDisability ?: "No"
    }
    private fun getSelectedOrphan(): String {
        return selectedOrphan ?: "No"
    }

    private fun handleOrphanSelection() {
        val selectedId: Int = binding.etOrphanStudentRadio.checkedRadioButtonId
        if (selectedId != -1) {
            val selectedRadioButton: RadioButton = binding.root.findViewById(selectedId)
            selectedOrphan = selectedRadioButton.text.toString()
//            Toast.makeText(activity, "Selected: $selectedGender", Toast.LENGTH_SHORT).show()
        } else {
//            Toast.makeText(activity, "No gender selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleDisablitySelection() {
        val selectedId: Int = binding.etPhiscalDisabilityRadio.checkedRadioButtonId
        if (selectedId != -1) {
            val selectedRadioButton: RadioButton = binding.root.findViewById(selectedId)
            selectedDisability = selectedRadioButton.text.toString()
            // Toast.makeText(activity, "Selected: $selectedGender", Toast.LENGTH_SHORT).show()
        } else {
            // Toast.makeText(activity, "No gender selected", Toast.LENGTH_SHORT).show()
        }
    }



    private fun addStudent() {

        try {
            val studentMap = mapOf(
                "school_id" to SchoolId().getSchoolId(requireContext()),
                "st_name" to binding.etStudentName.text.toString(),
                "registration_no" to binding.etregistrationNo.text.toString(),
                "st_class" to binding.spStudentSelectClass2.selectedItem.toString(),
               // "picture" to ImageConverter().convertImageViewToBase64(binding.imageStudent),
                "dt_of_admission" to binding.etDateOfAdmission.text.toString(),
                "discount_fee" to binding.etDiscountFees.text.toString(),
                "number" to binding.etPhoneNumber.text.toString(),
                "dt_of_birth" to edDateOfBirth.text.toString(),
                "identification_mark" to binding.edIdentificationMark.text.toString(),
                "religion" to binding.spSelectReligion.selectedItem.toString(),
                "blood_group" to binding.spSelectBloodGroup.selectedItem.toString(),
                "orphan_student" to getSelectedOrphan(),
                "gender" to getSelectedGender(),
                "cast" to binding.spSelectCast.selectedItem.toString(),
                "osc" to getSelectedDisablity(),
                "previous_id" to binding.etPreviousId.text.toString(),
                "family" to binding.etSelectFamily.text.toString(),
                "disease_if_any" to binding.edDiseaseIfAny.text.toString(),
                "additional_note" to binding.etAnyAdditionalNote.text.toString(),
                "siblings" to binding.etTotalSibling.text.toString(),
                "address" to binding.etAddress.text.toString(),
                "st_birth_id" to binding.etBirthId.text.toString(),

                "father_name" to binding.etFatherName.text.toString(),
                "father_id" to binding.etFatherID.text.toString(),
                "father_education" to binding.etFatherEducatino.text.toString(),
                "father_occupation" to binding.etFatherOccupation.text.toString(),
                "father_mobile" to binding.etFatherMobile.text.toString(),
                "father_profession" to binding.etFatherProfession.text.toString(),
                "father_income" to binding.etFatherIncome.selectedItem.toString(),

                "mother_name" to binding.etMotherName.text.toString(),
                "mother_id" to binding.etMotherID.text.toString(),
                "mother_mobile" to binding.etMotherMobile.text.toString(),
                "mother_occupation" to binding.etMotherOccupation.text.toString(),
                "mother_profession" to binding.etMotherProfession.text.toString(),
                "mother_income" to binding.etMotherIncome.selectedItem.toString(),
                "mother_education" to binding.etMotherEducation.text.toString(),

                "username" to binding.etPhoneNumber.text.toString(),
                "password" to binding.etStudentName.text.toString().substring(0,3)+binding.etPhoneNumber.text.toString().substring(6),
                "st_status" to "Active",

                "st_previous_school" to binding.etPreviousSchool.text.toString(),
                "created_at" to binding.etcreatedAt.text.toString(),
                "driver_name" to binding.onlyId.selectedItem.toString()
            )
            Log.d("AddStudent", "Student data as Map: $studentMap")
            viewModel.addStudent(studentMap)
        }catch (E:Exception){

        }


    }

    private fun observeViewModel() {
        viewModel.response.observe(viewLifecycleOwner, Observer { apiResponse ->
            apiResponse?.let {
                toolBox.showConfirmationDialog(
                    context = requireContext(),
                    title = "Student Added Successfully",
                    message = "Would you like to add more student ?",
                    positiveButtonText = "YES",
                    positiveButtonAction = { toolBox.replaceFragment(AddStudent(),requireContext()) },
                    negativeButtonText = "no",
                    negativeButtonAction = { toolBox.replaceFragment(AllStudent(),requireContext()) },
                    cancelable = false )
//                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_SHORT).show()
        })


    }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun setupAllClassObserver() {
        try{
            val factory = AllClassViewModelFactory(AllClassRepository())
            viewModelAllClass = ViewModelProvider(this, factory).get(AllClassViewModel::class.java)
            // Observe the LiveData from ViewModel
            viewModelAllClass.getClasses(SchoolId().getSchoolId(requireContext())).observe(viewLifecycleOwner) { response ->
                response?.data?.let { classList ->
                    if (classList.isNotEmpty()) {
                        // Extract the class names and class IDs from the classList
                        val classNames = mutableListOf("Select Class") // Add "Select Class" as the default item
                        val classIds = mutableListOf("Select Class ID") // Add "Select Class ID" as the default item

                        // Add class names and class IDs from the class list
                        classNames.addAll(classList.map { it.class_name })
                        classIds.addAll(classList.map { it.class_id.toString() })

                        // Create adapter for the first spinner with class names (including "Select Class")
                        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, classNames)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                        // Create adapter for the second spinner with class IDs (including "Select Class ID")
                        val adapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, classIds)
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                        // Set the adapters to the respective Spinners
                        binding.spStudentSelectClass.adapter = adapter
                        binding.spStudentSelectClass2.adapter = adapter2

                        // Set the OnItemSelectedListener for the first Spinner
                        binding.spStudentSelectClass.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parentView: AdapterView<*>, view: View?, position: Int, id: Long) {
                                if (position > 0){
                                    validation.updateSpinnerValidation(binding.spStudentSelectClassLayout, false)
                                }
                                val selectedClassName = parentView.getItemAtPosition(position) as String

                                val filteredClassIds = classList.filter { it.class_name == selectedClassName }
                                    .map { it.class_id.toString() }

                                if (filteredClassIds.isNotEmpty()) {
                                    val updatedAdapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, filteredClassIds)
                                    updatedAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                    binding.spStudentSelectClass2.adapter = updatedAdapter2
                                } else {
                                    binding.spStudentSelectClass2.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOf("Select Class ID"))
                                }
                            }

                            override fun onNothingSelected(parentView: AdapterView<*>) {

                                binding.spStudentSelectClass2.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOf("Select Class ID"))
                            }
                        }
                    } else {
                        Toast.makeText(requireContext(), "No classes found", Toast.LENGTH_SHORT).show()
                    }
                } ?: run {
                    toolBox.showConfirmationDialog(
                        context = requireContext(),
                        title = "No classes are available.",
                        message = "Would you like to add a class?",
                        positiveButtonText = "YES",
                        positiveButtonAction = { toolBox.fragmentChanger(NewClass(), requireContext()) },
                        negativeButtonText = "NO",
                        negativeButtonAction = { toolBox.activityChanger(MainActivity(), requireContext()) },
                        cancelable = false)
                }
            }
        }catch (E : Exception){
            toolBox.showConfirmationDialog(
                context = requireContext(),
                title = "Warning !",
                message = "Unknown Error found Please Contact with developer nurkude@gmail.com",
                positiveButtonText = "OK",
                positiveButtonAction = { toolBox.activityChanger(MainActivity(), requireContext()) },
                negativeButtonText = "",
                negativeButtonAction = { },
                cancelable = false)
            //Toast.makeText(requireContext(), "Unknow Error", Toast.LENGTH_SHORT).show()
        }

    }
    //  ---------------Start Calling class API to show all class data in spinner ----------

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        } else {
            Toast.makeText(requireContext(), "Camera not available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    // Get the image captured by the camera
                    val photo: Bitmap = data.extras?.get("data") as Bitmap
                    imageView.setImageBitmap(photo)
                }
                GALLERY_REQUEST_CODE -> {
                    // Get the image picked from the gallery
                    val selectedImageUri: Uri = data.data ?: return
                    try {
                        val selectedImageBitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, selectedImageUri)
                        imageView.setImageBitmap(selectedImageBitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun Validation(): Boolean {
        val fieldsToValidate = listOf(
            binding.etStudentName,
            binding.etregistrationNo,
            binding.edDateOfBirth,
            binding.etDateOfAdmission,
            binding.etDiscountFees,
            binding.etPhoneNumber,
//            binding.etOrphanStudent,
//            binding.etOSC,
//            binding.etPreviousId,
//            binding.etSelectFamily,
//            binding.edDiseaseIfAny,
//
//            binding.etAnyAdditionalNote,
//            binding.etTotalSibling,
//            binding.etAddress,
//
//            binding.etFatherName,
//            binding.etFatherID,
//            binding.etFatherEducatino,
//            binding.etFatherOccupation,

        )
        if(validation.validateTextInputFields(*fieldsToValidate.toTypedArray())){
            if (validateSpinners()){
                if (validation.validateGenderSelection(binding.radioGroupGender, "Select Gender", requireContext())){
                    return true
                }
            }
        }
        return false
    }
    private fun validateSpinners(): Boolean {
        if (binding.spStudentSelectClass.selectedItemPosition == 0) {
            validation.updateSpinnerValidation(binding.spStudentSelectClassLayout, true)
            Toast.makeText(requireContext(), "Please select a class", Toast.LENGTH_SHORT).show()
            return false
        }
//        if(validation.SpinnerValidation(binding.spStudentSelectClass, binding.spStudentSelectClassLayout, validation, "Please select a class", requireContext())){ return false }
//        if(validation.SpinnerValidation(binding.spSelectCast, binding.spSelectCastLayout, validation, "Please select a caste", requireContext())){ return false }
//        if(validation.SpinnerValidation(binding.spSelectReligion, binding.spSelectReligionLayout, validation, "Please select a religion", requireContext())){ return false }
//        if(validation.SpinnerValidation(binding.etFatherIncome, binding.etFatherIncomeLayout, validation, "Please select father's income", requireContext())){ return false }
//        if(validation.SpinnerValidation(binding.etMotherIncome, binding.etMotherIncomeLayout, validation, "Please select mother's income", requireContext())){ return false }
//        if (binding.spSelectCast.selectedItemPosition == 0) {
//            validation.updateSpinnerValidation(binding.spSelectCastLayout, true)
//            Toast.makeText(requireContext(), "Please select a caste", Toast.LENGTH_SHORT).show()
//            return false
//        }
//        if (binding.spSelectReligion.selectedItemPosition == 0) {
//            validation.updateSpinnerValidation(binding.spSelectReligionLayout, true)
//            Toast.makeText(requireContext(), "Please select a religion", Toast.LENGTH_SHORT).show()
//            return false
//        }
//        if (binding.etFatherIncome.selectedItemPosition == 0) {
//            validation.updateSpinnerValidation(binding.etFatherIncomeLayout, true)
//            Toast.makeText(requireContext(), "Please select father's income", Toast.LENGTH_SHORT).show()
//            return false
//        }
//        if (binding.etMotherIncome.selectedItemPosition == 0) {
//            validation.updateSpinnerValidation(binding.etMotherIncomeLayout, true)
//            Toast.makeText(requireContext(), "Please select mother's income", Toast.LENGTH_SHORT).show()
//            return false
//        }
        return true
    }

    fun showConfirmationDialog(
        context: Context,
        positiveButtonAction: () -> Unit,
    ) {
        // Create the AlertDialog
        val dialog = AlertDialog.Builder(context)
            .setMessage("Are you sure you want to go?")
            .setCancelable(true)  // Makes the dialog cancellable (tapping outside or pressing back)
            .setPositiveButton("Yes") { dialogInterface, _ ->
                // Action for the "Yes" button
                positiveButtonAction() // Execute the custom action passed to the dialog
                dialogInterface.dismiss()
            }
            .setNegativeButton("No") { dialogInterface, _ ->
                // Action for the "No" button
                Toast.makeText(context, "You clicked No", Toast.LENGTH_SHORT).show()
                dialogInterface.dismiss()
            }
            .setOnCancelListener {
                toolBox.fragmentChanger(AddStudent(),requireContext())
            }
            .create()

        // Show the dialog
        dialog.show()
    }
}
