package com.example.buttraking.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.buttraking.IDDetail.SchoolId
import com.example.buttraking.R
import com.example.buttraking.activity.MainActivity
import com.example.buttraking.api.RetrofitHelper
import com.example.buttraking.databinding.FragmentEditStudentBinding
import com.example.buttraking.dataclass.Student
import com.example.buttraking.helper.MethodLibrary
import com.example.buttraking.repository.AllClassRepository
import com.example.buttraking.repository.EditStudentRepository
import com.example.buttraking.repository.getDriverdataRepository
import com.example.buttraking.viewmodel.AllClassViewModel
import com.example.buttraking.viewmodel.EditStudentViewModel
import com.example.buttraking.viewmodel.getDriverdataViewModel
import com.example.buttraking.viewmodelfactory.AllClassViewModelFactory
import com.example.buttraking.viewmodelfactory.EditStudentViewModelFactory
import com.example.buttraking.viewmodelfactory.getdriverdataViewModelFactory
import java.io.IOException

class EditStudent : Fragment() {
    private lateinit var binding: FragmentEditStudentBinding
    private lateinit var viewModel: EditStudentViewModel
    private lateinit var viewModelAllClass: AllClassViewModel
    private var selectedGender: String? = null
    private lateinit var edDateOfBirth: EditText
    private lateinit var edDateOfAdmission: EditText
    private lateinit var ClassName: String
    private lateinit var profilePicture: String
    private var toolBox = MethodLibrary()
    private var selectedDisability: String? = null
    private var selectedOrphan: String? = null

    private lateinit var imageView: ImageView

    private lateinit var imageViewOne: ImageView


    companion object {
        const val REQUEST_IMAGE_CAPTURE_1: Int = 1
        const val REQUEST_IMAGE_GALLERY_1: Int = 11
    }
    private val PERMISSION_REQUEST_CODE = 103
    var photo1: Bitmap? = null
    private lateinit var viewmodledriverdata: getDriverdataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentEditStudentBinding.bind(inflater.inflate(R.layout.fragment_edit_student, null))

        setupAllClassObserver()

        binding.btnUpload.setOnClickListener {
            showImagePickerDialog(REQUEST_IMAGE_CAPTURE_1, REQUEST_IMAGE_GALLERY_1)
//            imageView.setImageDrawable(null)
            //openImageChooser()
//                showImagePickerDialog(
//                    EditEmp.REQUEST_IMAGE_CAPTURE_1, EditEmp.REQUEST_IMAGE_GALLERY_1
//                )
        }

        val apiService = RetrofitHelper.getApiService()
        val repository = EditStudentRepository(apiService)
        val factory = EditStudentViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(EditStudentViewModel::class.java)
        arguments?.getParcelable<Student>("student")?.let { student ->
            populateFields(student)
        }

        imageView = binding.imageStudent


        SetUpLisners()
        setupdriverviewmodel()
        setupDriverSpinner()

        return binding.root
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

    private fun getSelectedDisablity(): String {
        return selectedDisability ?: "No"
    }
    private fun getSelectedOrphan(): String {
        return selectedOrphan ?: "No"
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


    private fun populateFields(student: Student) {
        toolBox.startLoadingBar("Loading...", false, requireContext())

       // toolBox.displayImage("${ImageUtil.BASE_URL_IMAGE}student/${student.picture}", binding.imageStudentPreview, binding.root.context)

        binding.etschoolId.setText(student.school_id)
        binding.etStudentName.setText(student.st_name)
        binding.etregistrationNo.setText(student.registration_no)
        binding.spStudentSelectClass.setSelection(
            getSpinnerIndex(
                binding.spStudentSelectClass,
                student.st_class
            )
        )
        binding.etDateOfAdmission.setText(student.dt_of_admission)
        binding.etDiscountFees.setText(student.discount_fee)
        binding.etPhoneNumber.setText(student.number)
        binding.edDateOfBirth.setText(student.dt_of_birth)
        binding.spSelectReligion.setSelection(
            getSpinnerIndex(
                binding.spSelectReligion,
                student.religion
            )
        )
        binding.spSelectBloodGroup.setSelection(
            getSpinnerIndex(
                binding.spSelectBloodGroup,
                student.blood_group
            )
        )
        when (student.orphan_student) {
            "Yes" -> binding.orphanYes.isChecked = true
            "No" -> binding.orphanNo.isChecked = true
        }

        when (student.osc) {
            "Yes" -> binding.disableYes.isChecked = true
            "No" -> binding.disableNo.isChecked = true
        }

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
        binding.etBirthId.setText(student.st_birth_id)

        binding.etAnyIdentiMark.setText(student.identification_mark)
        binding.etPreviousSchool.setText(student.st_previous_school)
        binding.etcreatedAt.setText(student.created_at)
        binding.TxtId.setText(student.id)
        binding.etFatherMobile.setText(student.father_mobile)
        binding.etFatherEducatino.setText(student.father_education)
        binding.etFatherProfession.setText(student.father_profession)

        binding.etMotherName.setText(student.mother_name)
        binding.etMotherID.setText(student.mother_id)
        binding.etMotherEducation.setText(student.mother_education)
        binding.etMotherMobile.setText(student.mother_mobile)
        binding.etMotherOccupation.setText(student.mother_occupation)
        binding.etMotherProfession.setText(student.mother_profession)

        toolBox.SetSpinnerByName(
            spinner = binding.spSelectReligion,
            setDefaultName = student.religion.toString(),
            context = requireContext(),
            arrayResourceId = R.array.select_Religion_Employees
        )

        toolBox.SetSpinnerByName(
            spinner = binding.spSelectCast,
            setDefaultName = student.cast.toString(),
            context = requireContext(),
            arrayResourceId = R.array.select_Cast
        )

        toolBox.SetSpinnerByName(
            spinner = binding.spSelectBloodGroup,
            setDefaultName = student.blood_group.toString(),
            context = requireContext(),
            arrayResourceId = R.array.select_Blood_Group_Employees
        )

        toolBox.SetSpinnerByName(
            spinner = binding.etFatherIncome,
            setDefaultName = student.father_income.toString(),
            context = requireContext(),
            arrayResourceId = R.array.select_income
        )

        toolBox.SetSpinnerByName(
            spinner = binding.etMotherIncome,
            setDefaultName = student.mother_income,
            context = requireContext(),
            arrayResourceId = R.array.select_income
        )

        ClassName = student.class_name

        profilePicture = student.picture

        // Handle setting gender (assuming radio buttons or dropdown)
        setSelectedGender(student.gender)
        //setSelectedGender(student.)

        toolBox.stopLoadingBar()
        // Load the image if available
        /*if (student.picture.isNotEmpty()) {
            val imageUri = Uri.parse(student.picture)
            binding.imageView.setImageURI(imageUri)  // Assuming `imageView` is the ImageView for the picture
        }*/
    }

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


    private fun handleGenderSelection() {
        val selectedId: Int = binding.radioGroupGender.checkedRadioButtonId
        if (selectedId != -1) {
            val selectedRadioButton: RadioButton = binding.root.findViewById(selectedId)
            selectedGender = selectedRadioButton.text.toString()
//            Toast.makeText(activity, "Selected: $selectedGender", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity, "No gender selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun SetUpLisners() {
        if (imageView.getDrawable() != null) {
            // Image is set
        } else {
            // No image is set
        }

        binding.btnSubmit.setOnClickListener {
            if (!toolBox.isInternetAvailable(requireContext())){
                toolBox.deviceOffLineMassage(requireContext())
            }else{
                handleOrphanSelection()
                handleDisablitySelection()
                handleGenderSelection()
                EditStudent1()
            }

        }
        edDateOfBirth = binding.edDateOfBirth
        edDateOfAdmission = binding.etDateOfAdmission
        imageView = binding.imageStudent  // Correctly reference the ImageView ID

        edDateOfBirth.setOnClickListener { toolBox.DatePicker(edDateOfBirth, requireContext())  }
        edDateOfAdmission.setOnClickListener { toolBox.DatePicker(edDateOfAdmission, requireContext()) }


    }

    private fun getSelectedGender(): String {
        return selectedGender ?: "Not specified"
    }

    private fun EditStudent1() {

        if (binding.imageStudent.getDrawable() != null) {
            imageViewOne = binding.imageStudentPreview
            imageViewOne = binding.imageStudent
            val EditStudentMap = mapOf(
                "school_id" to SchoolId().getSchoolId(requireContext()),
                "st_name" to (binding.etStudentName.text?.toString() ?: ""),
                "registration_no" to (binding.etregistrationNo.text?.toString() ?: ""),
                "st_class" to (binding.spStudentSelectClass2.selectedItem?.toString() ?: ""),
                // "picture" to (ImageUtil.getBase64StringFromImageView(binding.imageStudent) ?: ""),
              //  "picture" to (ImageUtil.getBase64StringFromImageView(binding.imageStudent) ?: ""),
                "dt_of_admission" to (binding.etDateOfAdmission.text?.toString() ?: ""),
                "discount_fee" to (binding.etDiscountFees.text?.toString() ?: ""),
                "identification_mark" to (/*binding.etIdentificationMark.text?.toString() ?:*/ "kkj"),
                "st_birth_id" to (binding.etBirthId.text?.toString() ?: ""),
                "st_previous_school" to (binding.etPreviousSchool.text?.toString() ?: ""),
                "number" to (binding.etPhoneNumber.text?.toString() ?: ""),
                "dt_of_birth" to (edDateOfBirth.text?.toString() ?: ""),
                "religion" to (binding.spSelectReligion.selectedItem?.toString() ?: ""),
                "blood_group" to (binding.spSelectBloodGroup.selectedItem?.toString() ?: ""),
                "orphan_student" to (getSelectedOrphan()),
                "gender" to getSelectedGender(),
                "cast" to (binding.spSelectCast.selectedItem?.toString() ?: ""),
                "osc" to (getSelectedDisablity()),
                "address" to (binding.etAddress.text?.toString() ?: ""),
                "father_name" to (binding.etFatherName.text?.toString() ?: ""),
                "father_id" to (/*binding.etFatherId.text?.toString() ?:*/ "kk"),
                "father_education" to (/*binding.etFatherEducation.text?.toString() ?:*/ "kkf"),
                "father_mobile" to (binding.etFatherMobile.text?.toString() ?: ""),
                "father_profession" to (binding.etFatherProfession.text?.toString() ?: ""),
                "father_income" to (binding.etFatherIncome.selectedItem?.toString() ?:""),
                "father_occupation" to (binding.etFatherOccupation.text?.toString() ?: ""),
                "mother_name" to (binding.etMotherName.text?.toString() ?: ""),
                "mother_id" to (/*binding.etMotherId.text?.toString() ?:*/ "Not Available"),
                "mother_mobile" to (binding.etMotherMobile.text?.toString() ?: ""),
                "mother_occupation" to (binding.etMotherOccupation.text?.toString() ?: ""),
                "mother_income" to (binding.etMotherIncome.selectedItem?.toString() ?: "Not Available"),
                "mother_education" to (binding.etMotherEducation.text?.toString() ?: ""),
                "mother_profession" to (binding.etMotherProfession.text?.toString() ?: ""),
                "Status" to "Active",
                "id" to (binding.TxtId.text?.toString() ?: ""),
                "created_at" to (binding.etcreatedAt.text?.toString() ?: ""),
                "username" to (binding.etPhoneNumber.text.toString()),
                "password" to (binding.etStudentName.text.toString().substring(0,3)+binding.etPhoneNumber.text.toString().substring(6)),

                "disease_if_any" to (binding.edDiseaseIfAny.text?.toString() ?: ""),
                "siblings" to (binding.etTotalSibling.text?.toString() ?: ""),
                "st_status" to "Active",
                "additional_note" to (binding.etAnyAdditionalNote.text?.toString() ?: ""),
                "previous_school" to (binding.etPreviousSchool.text?.toString() ?: ""),
                // "identity_mark" to (binding.etcreatedAt.text?.toString() ?: ""),
                "family" to (binding.etSelectFamily.text?.toString() ?: ""),
                "previous_id" to (binding.etPreviousId.text?.toString() ?: ""),
                "driver_name" to binding.onlyId.selectedItem.toString()

            )
            //Toast.makeText(requireContext(), "Student Updated Successfully", Toast.LENGTH_SHORT).show()
            toolBox.showConfirmationDialog(
                context = requireContext(),
                title = "Student Data Updated Successfully",
                message = "Would you like to view the student list?",
                positiveButtonText = "ok",
                positiveButtonAction = { toolBox.activityChanger(MainActivity(), requireContext()) },
                negativeButtonText = "view",
                negativeButtonAction = { toolBox.replaceFragment(AllStudent(), requireContext()) },
                cancelable = false)
            Log.d("EditStudent", "Student data as Map: $EditStudentMap")
            viewModel.editStudent(EditStudentMap)


        }else{
            val EditStudentMap = mapOf(
                "school_id" to SchoolId().getSchoolId(requireContext()),
                "st_name" to (binding.etStudentName.text?.toString() ?: ""),
                "registration_no" to (binding.etregistrationNo.text?.toString() ?: ""),
                "st_class" to (binding.spStudentSelectClass2.selectedItem?.toString() ?: ""),
                // "picture" to (ImageUtil.getBase64StringFromImageView(binding.imageStudent) ?: ""),
              //  "picture" to (ImageUtil.getBase64StringFromImageView(binding.imageStudentPreview) ?: ""),
                "dt_of_admission" to (binding.etDateOfAdmission.text?.toString() ?: ""),
                "discount_fee" to (binding.etDiscountFees.text?.toString() ?: ""),
                "identification_mark" to (/*binding.etIdentificationMark.text?.toString() ?:*/ "kkj"),
                "st_birth_id" to (binding.etBirthId.text?.toString() ?: ""),
                "st_previous_school" to (binding.etPreviousSchool.text?.toString() ?: ""),
                "number" to (binding.etPhoneNumber.text?.toString() ?: ""),
                "dt_of_birth" to (edDateOfBirth.text?.toString() ?: ""),
                "religion" to (binding.spSelectReligion.selectedItem?.toString() ?: ""),
                "blood_group" to (binding.spSelectBloodGroup.selectedItem?.toString() ?: ""),
                "orphan_student" to (getSelectedOrphan()),
                "gender" to getSelectedGender(),
                "cast" to (binding.spSelectCast.selectedItem?.toString() ?: ""),
                "osc" to (getSelectedDisablity()),
                "address" to (binding.etAddress.text?.toString() ?: ""),
                "father_name" to (binding.etFatherName.text?.toString() ?: ""),
                "father_id" to (/*binding.etFatherId.text?.toString() ?:*/ "kk"),
                "father_education" to (/*binding.etFatherEducation.text?.toString() ?:*/ "kkf"),
                "father_mobile" to (binding.etFatherMobile.text?.toString() ?: ""),
                "father_profession" to (binding.etFatherProfession.text?.toString() ?: ""),
                "Status" to "Active",
                "father_income" to (binding.etFatherIncome.selectedItem?.toString() ?:""),
                "father_occupation" to (binding.etFatherOccupation.text?.toString() ?: ""),
                "mother_name" to (binding.etMotherName.text?.toString() ?: ""),
                "mother_id" to (/*binding.etMotherId.text?.toString() ?:*/ "Not Available"),
                "mother_mobile" to (binding.etMotherMobile.text?.toString() ?: ""),
                "mother_occupation" to (binding.etMotherOccupation.text?.toString() ?: ""),
                "mother_income" to (binding.etMotherIncome.selectedItem?.toString() ?: "Not Available"),
                "mother_education" to (binding.etMotherEducation.text?.toString() ?: ""),
                "mother_profession" to (binding.etMotherProfession.text?.toString() ?: ""),
                "st_status" to "Active",
                "id" to (binding.TxtId.text?.toString() ?: ""),
                "created_at" to (binding.etcreatedAt.text?.toString() ?: ""),
                "username" to (binding.etPhoneNumber.text.toString()),
                "password" to (binding.etStudentName.text.toString().substring(0,3)+binding.etPhoneNumber.text.toString().substring(6)),
                "disease_if_any" to (binding.edDiseaseIfAny.text?.toString() ?: ""),
                "siblings" to (binding.etTotalSibling.text?.toString() ?: ""),
                "st_status" to "Active",
                "additional_note" to (binding.etAnyAdditionalNote.text?.toString() ?: ""),
                "previous_school" to (binding.etPreviousSchool.text?.toString() ?: ""),
                // "identity_mark" to (binding.etcreatedAt.text?.toString() ?: ""),
                "family" to (binding.etSelectFamily.text?.toString() ?: ""),
                "previous_id" to (binding.etPreviousId.text?.toString() ?: ""),
                "driver_name" to binding.onlyId.selectedItem.toString()

            )

            //  Toast.makeText(requireContext(), "Student Updated Successfully", Toast.LENGTH_SHORT).show()

            toolBox.showConfirmationDialog(
                context = requireContext(),
                title = "Student Data Updated Successfully",
                message = "Would you like to view the student list?",
                positiveButtonText = "ok",
                positiveButtonAction = { toolBox.activityChanger(MainActivity(), requireContext()) },
                negativeButtonText = "view",
                negativeButtonAction = { toolBox.replaceFragment(AllStudent(), requireContext()) },
                cancelable = false)
            Log.d("EditStudent", "Student data as Map: $EditStudentMap")
            viewModel.editStudent(EditStudentMap)
        }

//        else {
//            Toast.makeText(requireContext(), "image not available", Toast.LENGTH_SHORT).show()
//            imageViewOne = binding.imageStudent
//        }


    }

    private fun setupAllClassObserver() {
        try {
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

                        try {
                            // Set the default selection to "KG-2" if it exists
                            val selectedClassIndex = classNames.indexOf(ClassName)
                            if (selectedClassIndex != -1) {
                                binding.spStudentSelectClass.setSelection(selectedClassIndex)
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
                            //Toast.makeText(requireContext(), E.message, Toast.LENGTH_SHORT).show()
                        }


                        // Set the OnItemSelectedListener for the first Spinner
                        binding.spStudentSelectClass.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parentView: AdapterView<*>, view: View?, position: Int, id: Long) {
                                if (position > 0) {
                                    // validation.updateSpinnerValidation(binding.spStudentSelectClassLayout, false)
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
                        title = "Warning !",
                        message = "Unknown Error found Please Contact with developer nurkude@gmail.com",
                        positiveButtonText = "OK",
                        positiveButtonAction = { toolBox.activityChanger(MainActivity(), requireContext()) },
                        negativeButtonText = "",
                        negativeButtonAction = { },
                        cancelable = false)
                }
            }
        } catch (E: Exception) {
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

    private fun showImagePickerDialog(requestImageCapture: Int, requestImageGallery: Int) {
        val options = arrayOf("Camera", "Choose from Gallery")
        AlertDialog.Builder(requireContext())
            .setTitle("Pick an Image")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> captureImage(requestImageCapture)
                    1 -> openGallery(requestImageGallery)
                }
            }
            .show()
    }

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

    private fun openGallery(requestGallery: Int) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, requestGallery)
    }

/*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            when (requestCode) {
                */
/*SendMessageAdmin.REQUEST_IMAGE_CAPTURE_1 -> {
                    val photo = data?.extras?.get("data") as? Bitmap
                    photo?.let {
                        Log.d("onActivityResult", "Captured Image Bitmap: $it")

                        photo1 = it // Save the captured photo for later Base64 conversion

                        // Show the captured image in the ImageView
                        binding.imageStudent.setImageBitmap(it)  // Set the captured image to ImageView
                        binding.imageStudentPreview.visibility = View.GONE
                        binding.imageStudent.visibility = View.VISIBLE
                        //   Toast.makeText(requireContext(), "Image from Camera set", Toast.LENGTH_SHORT).show()
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

                            // Show the selected image in the ImageView
                            binding.imageStudent.setImageBitmap(photo)  // Set the selected image to ImageView
                            binding.imageStudentPreview.visibility = View.GONE
                            binding.imageStudent.visibility = View.VISIBLE
                            //     Toast.makeText(requireContext(), "Image from Gallery set", Toast.LENGTH_SHORT).show()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }*//*

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
*/

}