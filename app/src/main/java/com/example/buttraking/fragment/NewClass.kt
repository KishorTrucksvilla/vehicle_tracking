package com.example.buttraking.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buttraking.IDDetail.SchoolId
import com.example.buttraking.R
import com.example.buttraking.activity.MainActivity
import com.example.buttraking.api.RetrofitHelper
import com.example.buttraking.databinding.FragmentNewClassBinding
import com.example.buttraking.helper.MethodLibrary
import com.example.buttraking.helper.validation
import com.example.buttraking.model.response.AllClassNameResponse
import com.example.buttraking.model.response.TeacherNameResponse
import com.example.buttraking.repository.AllClassRepository
import com.example.buttraking.repository.ClassRepository
import com.example.buttraking.viewmodel.AllClassViewModel
import com.example.buttraking.viewmodel.ClassViewModel
import com.example.buttraking.viewmodelfactory.AllClassViewModelFactory
import com.example.buttraking.viewmodelfactory.ClassViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewClass : Fragment() {
    private lateinit var binding: FragmentNewClassBinding
    private var teacherName: String = ""
    private val stringList: ArrayList<String> = arrayListOf()
    private lateinit var viewModelAllClass: AllClassViewModel
    var toolbox = MethodLibrary()
    var valida = validation()
    private val classNamesList = mutableListOf<String>()

    // Initialize ViewModel
    private lateinit var viewModel: ClassViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewClassBinding.inflate(inflater, container, false)
        //setvieenewmodel()

        toolbox.startLoadingBar("Loading", false, requireContext())

        toolbox.clicked(binding.tvTeacherNotAvailable) {
            toolbox.fragmentChanger(
                AddNewEmployees(),
                requireContext()
            )
        }
        // Set up submit button click listener
        // binding.spSelectTeacherLayout.setBackground(getResources().getColor(R.color.my_background_color, null););

//        binding.etClassTeacher.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
//                // Handle the item selection here
//                val selectedItem = parentView?.getItemAtPosition(position).toString()
//                // Show a toast with the selected item
//                Toast.makeText(requireContext(), "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onNothingSelected(parentView: AdapterView<*>?) {
//                Toast.makeText(requireContext(), "Selected: NOT", Toast.LENGTH_SHORT).show()
//            }
//        }

        binding.btnCreateClas.setOnClickListener {
            if (!toolbox.isInternetAvailable(requireContext())){
                toolbox.deviceOffLineMassage(requireContext())
            }else{
                if (Validation()) {
                    if (!binding.etClassTeacher.selectedItem.toString().equals("Select Teacher")) {
                        fetchClassNames(SchoolId().getSchoolId(requireContext()))
                        stringList.addAll(classNamesList)
                        submitClassData()
                    }
                }
            }
        }
        fetchTeacherNames(SchoolId().getSchoolId(requireContext()))
        setupviewmodel()
        setvieenewmodel()
        return binding.root
    }
    private fun setvieenewmodel() {
        val apiService = RetrofitHelper.getApiService()
        val repository = ClassRepository(apiService)
        val factory = ClassViewModelFactory(repository)  // Use the correct factory
        viewModel = ViewModelProvider(this, factory).get(ClassViewModel::class.java)
    }

    private fun setupviewmodel() {
        val repository = AllClassRepository()
        val factory = AllClassViewModelFactory(repository)
        viewModelAllClass = ViewModelProvider(this, factory).get(AllClassViewModel::class.java)
    }

    private fun submitClassData() {
        // Collect form data from input fields
        val className = binding.spClass.text.toString().trim()
        val tuitionFees = binding.edtTuitionfees.text.toString().trim()
        val selectedTeacher = binding.etClassTeacher2.selectedItem.toString()  // Get the selected teacher's name from the Spinner

        // Validate input data
        if (tuitionFees.isEmpty() || selectedTeacher.isEmpty()) {
            binding.edtTuitionfees.error = "Please enter tuition fees"
            Toast.makeText(activity, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        binding.btnCreateClas.isEnabled = false

        // Prepare data to be sent to the ViewModel
        if (binding.spClass.text.toString().isNotEmpty()){
            val classData = HashMap<String, String>().apply {
                put("class_name", binding.spClass.text.toString().trim())
                put("tution_fees", binding.edtTuitionfees.text.toString().trim())
                put(
                    "class_teacher",
                    binding.etClassTeacher2.selectedItem.toString()
                )  // Use the selected teacher from Spinner
                put(
                    "school_id",
                    SchoolId().getSchoolId(requireContext())
                )  // Example School ID, replace as needed
            }
            viewModel.addClass(classData).observe(viewLifecycleOwner) { apiResponse ->
                if (apiResponse.status) {
                    toolbox.showConfirmationDialog(
                        context = requireContext(), // or requireContext() if in a Fragment
                        title = "Class Created Successfully",
                        message = "Would you like to add another class?",
                        positiveButtonText = "YES",
                        positiveButtonAction = {toolbox.replaceFragment(NewClass(),requireContext()) },
                        negativeButtonText = "NO",
                        negativeButtonAction = {
                            toolbox.replaceFragment(AllClass(), requireContext())
                            valida.updateSpinnerValidation(binding.spSelectTeacherLayout, false)
                        },
                        cancelable = false
                    )
                } else {
                    toolbox.showConfirmationDialog(
                        context = requireContext(),
                        title = "Warning !",
                        message = "Class already created Please Enter New Class.",
                        positiveButtonText = "OK",
                        positiveButtonAction = { },
                        cancelable = true
                    )
                }


                // Re-enable the button after the operation completes
                binding.btnCreateClas.isEnabled = true
            }
        }else{
            binding.spClass.error = "Please enter a valid class name "
        }


//      Method not allowed
        // Observe LiveData from ViewModel for class creation


    }

    private fun fetchTeacherNames(SchoolID: String) {
        RetrofitHelper.getApiService().getEmployeeData(SchoolID)
            .enqueue(object : Callback<TeacherNameResponse> {
                override fun onResponse(
                    call: Call<TeacherNameResponse>,
                    response: Response<TeacherNameResponse>
                ) {
                    try {
                        if (response.isSuccessful && response.body() != null) {
                            toolbox.stopLoadingBar()
                            if (response.isSuccessful) {
                                binding.mainLayout.visibility = View.VISIBLE
                                binding.tvTeacherNotAvailable.visibility = View.GONE
                            }
                            val employees = response.body()!!.data
                            val roles = mutableListOf<String>()
                            roles.add("Select Teacher")

                            // Create a map to store employee names and their corresponding IDs
                            val teacherIdMap = mutableMapOf<String, String>()

                            // Extracting employee names and populating the roles list
                            for (employee in employees) {
                                employee.employeeName?.let {
                                    roles.add(it)  // Add employee names to the list
                                    teacherIdMap[it] = employee.id.orEmpty()
                                }
                            }
                            if (roles.isEmpty()) {
                                Toast.makeText(
                                    requireContext(),
                                    "No employee names available",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            // Set up the adapter to populate the Teacher Spinner
                            val adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                roles
                            )
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.etClassTeacher.adapter = adapter

                            // Automatically select the first item in the second spinner based on the first spinner selection
                            binding.etClassTeacher.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        if (!binding.etClassTeacher.selectedItem.toString()
                                                .equals("Select Teacher")
                                        ) {
                                            valida.updateSpinnerValidation(
                                                binding.spSelectTeacherLayout,
                                                false
                                            )
//                                    Toast.makeText(requireContext(), "Hello", Toast.LENGTH_SHORT).show()
                                        }


                                        if (position > 0) {
                                            val selectedTeacher =
                                                roles[position] // The selected teacher name
                                            val teacherId =
                                                teacherIdMap[selectedTeacher] // Get the corresponding teacher ID
                                            // Call a function to populate the second spinner with teacher IDs
                                            populateSecondSpinner(teacherId)
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        // Handle case when nothing is selected
                                    }
                                }

                        } else {
                            toolbox.stopLoadingBar()
                            Toast.makeText(requireContext(), "Failed to load data: ${response.message()}", Toast.LENGTH_SHORT).show()
                            Log.e("NewClass", "Error: ${response.message()}")
                        }
                    }catch (e: Exception) {
                        toolbox.showConfirmationDialog(
                            context = requireContext(),
                            title = "No teachers are available.",
                            message = "Would you like to add a teacher?",
                            positiveButtonText = "YES",
                            positiveButtonAction = { toolbox.fragmentChanger(AddNewEmployees(), requireContext()) },
                            negativeButtonText = "NO",
                            negativeButtonAction = { toolbox.activityChanger(MainActivity(), requireContext()) },
                            cancelable = false )
                    }
                }

                override fun onFailure(call: Call<TeacherNameResponse>, t: Throwable) {
                    toolbox.stopLoadingBar()
                    toolbox.showConfirmationDialog(
                        context = requireContext(),
                        title = "Failed to Load !",
                        message = "Connection failed please check mobile data or wifi and try again.",
                        positiveButtonText = "ok",
                        positiveButtonAction = { toolbox.activityChanger(MainActivity(), requireContext()) },
                        negativeButtonText = "Refresh",
                        negativeButtonAction = {toolbox.fragmentChanger(NewClass(), requireContext()) },
                        cancelable = false
                    )
                    // Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()

                    Log.e("NewClass", "Network error: ${t.message}")
                }
            })
    }

    private fun populateSecondSpinner(teacherId: String?) {
        if (teacherId != null) {
            val idList = listOf(teacherId)

            // Set up the adapter for the second spinner (etClassTeacher2)
            val idAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, idList)
            idAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.etClassTeacher2.adapter = idAdapter

            // Optionally, you can set a default selected ID
            binding.etClassTeacher2.setSelection(0)
        } else {
            Toast.makeText(requireContext(), "Invalid Teacher ID", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchClassNames(schoolId: String) {
        try {
            RetrofitHelper.getApiService().getClassList(schoolId)
                .enqueue(object : Callback<AllClassNameResponse> {
                    override fun onResponse(
                        call: Call<AllClassNameResponse>,
                        response: Response<AllClassNameResponse>
                    ) {
                        if (response.isSuccessful) {
                            val allClassNameResponse = response.body()

                            if (allClassNameResponse != null && allClassNameResponse.status) {
                                // Extract class names and store them in the list
                                val classNames = allClassNameResponse.classes.map { it.class_name }

                                // Update the classNamesList
                                classNamesList.clear()
                                classNamesList.addAll(classNames)

                                // Set up the adapter for the class names Spinner
                                val adapter = ArrayAdapter(
                                    requireContext(),
                                    android.R.layout.simple_spinner_item,
                                    classNames
                                )
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                binding.sampleSpinner.adapter = adapter
                            }
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Failed to load class names",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<AllClassNameResponse>, t: Throwable) {
                        toolbox.stopLoadingBar()
                        Toast.makeText(requireContext(), "Error:cxzfvcxvvxvds ${t.message}", Toast.LENGTH_SHORT)
                            .show()
                        Log.e("NewClass", "Network error: ${t.message}")
                    }
                })
        } catch (e: Exception) {
            toolbox.showConfirmationDialog(
                context = requireContext(),
                title = "Warning !",
                message = "Unknown Error found Please Contact with developer nurkude@gmail.com ",
                positiveButtonText = "ADD CLASS",
                positiveButtonAction = { toolbox.fragmentChanger(NewClass(), requireContext()) },
                negativeButtonText = "",
                negativeButtonAction = { },
                cancelable = false
            )
        }

    }

    private fun Validation(): Boolean {
        val className = binding.spClass.text.toString().trim()

        // Regex to check for valid class name without spaces (only alphabets)
        val classNamePattern = "^[a-zA-Z0-9\\-_.@!#\$%^&*()+=|<>?{}\\[\\]\\\\/~`]*$".toRegex()

//        if (!className.matches(classNamePattern)) {
//            binding.spClass.error = "Please enter a valid class name without spaces"
//            return false
//        }

        if (binding.edtTuitionfees.text.toString().trim().isEmpty()) {
            binding.edtTuitionfees.error = "Please enter tuition fees"
            return false
        }

        if (binding.etClassTeacher.selectedItem.toString().equals("Select Teacher")) {
            valida.updateSpinnerValidation(binding.spSelectTeacherLayout, true)
            Toast.makeText(requireContext(), "Please select a teacher", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}
