package com.example.buttraking.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.buttraking.IDDetail.SchoolId
import com.example.buttraking.R
import com.example.buttraking.adapter.AllEmployeeAdapter
import com.example.buttraking.api.RetrofitHelper
import com.example.buttraking.databinding.FragmentAllEmployeesBinding
import com.example.buttraking.dataclass.AllEmployee
import com.example.buttraking.helper.MethodLibrary
import com.example.buttraking.repository.AllEmployeesRepository
import com.example.buttraking.viewmodel.AllEmployeesViewModel
import com.example.buttraking.viewmodelfactory.AllEmployeesViewModelFactory

class AllEmployees : Fragment(), AllEmployeeAdapter.OnEmployeeActionListener {

    private lateinit var binding: FragmentAllEmployeesBinding
    private lateinit var employeeAdapter: AllEmployeeAdapter
    private val employeeList = mutableListOf<AllEmployee>()
    private lateinit var viewModel: AllEmployeesViewModel
    var toolbox = MethodLibrary()
    var toolBox = MethodLibrary()
    private lateinit var connectivityReceiver: BroadcastReceiver
    private var isDataObserved = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllEmployeesBinding.bind(
            inflater.inflate(R.layout.fragment_all_employees, null)
        )

        val apiService = RetrofitHelper.getApiService()
        val repository = AllEmployeesRepository(apiService)
        val factory = AllEmployeesViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(AllEmployeesViewModel::class.java)

        initView()
        observeData()

       // toolbox.clicked(binding.imgAddEmployees) { toolbox.fragmentChanger(AddNewEmployees(), requireContext()) }
        registerConnectivityReceiver()
        requireContext().registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        return binding.root
    }
    private fun registerConnectivityReceiver() {
        connectivityReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                context?.let {
                    if (isInternetAvailable(it)) {
                        observeData()
                    }
                }
            }
        }
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        requireContext().registerReceiver(connectivityReceiver, intentFilter)
    }
    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Unregister the receiver only if it has been initialized
        if (::connectivityReceiver.isInitialized) {
            requireContext().unregisterReceiver(connectivityReceiver)
        }
    }
    private fun initView() {
        val gridLayoutManager = GridLayoutManager(activity, 2)
        binding.recyclerViewAllEmployees.layoutManager = gridLayoutManager
        employeeAdapter = AllEmployeeAdapter(employeeList, this)
        binding.recyclerViewAllEmployees.adapter = employeeAdapter

        binding.searchViewEmployees.apply {
            // Ensure the SearchView expands and responds on the first click
            isIconified = false
            clearFocus()

            setOnClickListener {
                requestFocus() // Force focus on the SearchView
            }

            binding.searchViewEmployees.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.d("SearchView", "Search query changed: $newText")
                    employeeAdapter.filter(newText.orEmpty())
                    return true
                }
            })

            binding.searchViewEmployees.isIconified = true
            binding.searchViewEmployees.clearFocus()

            binding.searchViewEmployees.setOnClickListener{
                binding.searchViewEmployees.isFocusable = true
                binding.searchViewEmployees.isIconified = false
                binding.searchViewEmployees.requestFocus()
            }
        }
    }

    private fun toggleEmptyView() {
        toolbox.clicked(binding.txtForDefault) {
            if (employeeAdapter.itemCount == 0) {
                toolbox.fragmentChanger(AddNewEmployees(), requireContext())
            }
        }

        // Log the data size for debugging
        Log.d("EmployeeListSize", "List size: ${employeeList.size}")
        Log.d("AdapterItemCount", "Adapter count: ${employeeAdapter.itemCount}")

        if (employeeAdapter.itemCount == 0) {
            binding.txtForDefault.visibility = View.VISIBLE
            binding.recyclerViewAllEmployees.visibility = View.GONE
        } else {
            binding.txtForDefault.visibility = View.GONE
            binding.recyclerViewAllEmployees.visibility = View.VISIBLE
        }
    }

    private fun observeData() {
        if (isDataObserved) return

        if (!toolBox.isInternetAvailable(requireContext())) {
            toolBox.deviceOffLineMassage(requireContext())
            return
        }

        isDataObserved = true // Mark as observed
        viewModel.getAllEmployees(SchoolId().getSchoolId(requireContext())).observe(viewLifecycleOwner) { response ->
            if (response != null) {
                if (!response.employee.isNullOrEmpty()) {
                    employeeList.clear()
                    for (employee in response.employee) {
                        employeeList.add(
                            AllEmployee(
                                name = employee.employee_name ?: "Unknown Name",
                                title = employee.employee_role ?: "Unknown Role",
                                img = R.drawable.teacherprofile,
                                id = employee.id,
                                employee_name = employee.employee_name ?: "Unknown Name",
                                number = employee.number ?: "N/A",
                                employee_role = employee.employee_role ?: "Unknown Role",
                                picture = employee.picture ?: "default_picture_url_or_path",
                                date_of_joining = employee.date_of_joining ?: "N/A",
                                monthly_salary = employee.monthly_salary ?: "0",
                                f_h_name = employee.f_h_name ?: "Unknown",
                                national_id = employee.national_id ?: "N/A",
                                education = employee.education ?: "N/A",
                                gender = employee.gender ?: "N/A",
                                religion = employee.religion ?: "N/A",
                                blood_group = employee.blood_group ?: "N/A",
                                experience = employee.experience ?: "N/A",
                                date_of_birth = employee.date_of_birth ?: "N/A",
                                home_address = employee.home_address ?: "N/A",
                                email = employee.email ?: "N/A",
                                username = employee.username ?: "N/A",
                                password = employee.password ?: "N/A",
                                st_status = employee.st_status ?: "N/A",
                                school_id = employee.school_id ?: "N/A",
                                created_at = employee.created_at ?: "N/A",
                                school_name = employee.school_name ?: "N/A",
                                institute_logo = employee.institute_logo ?: "N/A",
                                employee_id = employee.employee_id ?: "N/A",
                            )
                        )
                    }
                    employeeAdapter.updateList(employeeList) // Update the adapter with new data
                    toggleEmptyView() // Call to update visibility based on data
                } else {
                    showAddEmployeesDialog()
                    Toast.makeText(requireContext(), "No employees found", Toast.LENGTH_SHORT).show()
                    toggleEmptyView() // Update UI visibility for empty state
                }
            } else {
                Toast.makeText(requireContext(), "Error fetching employee data", Toast.LENGTH_SHORT).show()
                toggleEmptyView() // Update UI visibility for error state
                showAddEmployeesDialog()
            }
        }
    }

    override fun onEditEmployee(employee: AllEmployee) {
        val schoolId = employee.school_id.trim()
        val employeeId = employee.id

        val bundle = Bundle().apply {
            putParcelable("employee", employee)
            putString("school_id", schoolId)
            putString("employee_id", employeeId)
        }

        val editEmpFragment = EditEmp().apply {
            arguments = bundle
        }

        val fragmentTransaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, editEmpFragment)
        //fragmentTransaction.addToBackStack(null) // Add the fragment to back stack
        fragmentTransaction.commit()
    }

    override fun onResume() {
        super.onResume()
        // Refresh the fragment content here, for example, fetching new data or updating UI
        refreshFragmentData()
    }

    private fun refreshFragmentData() {
        // Fetch or update data as necessary
        // Example: Call your ViewModel to fetch updated data
        val apiService = RetrofitHelper.getApiService()
        val repository = AllEmployeesRepository(apiService)
        val factory = AllEmployeesViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(AllEmployeesViewModel::class.java)
    }

    override fun onDeleteEmployee(employee: AllEmployee, position: Int) {
        toggleEmptyView()
        val schoolId = employee.school_id.trim()
        val employeeId = employee.id
        val employeeName = employee.name

        AlertDialog.Builder(requireContext())
            .setTitle("Delete $employeeName")
            .setMessage("Are you sure you want to delete employee?")
            .setPositiveButton("Yes") { dialog, which ->
                Log.d("DeleteEmployee", "Attempting to delete employee with ID: $employeeId, School ID: $schoolId")

                viewModel.deleteEmployee(schoolId, employeeId)
                    .observe(viewLifecycleOwner) { response ->
                        if (response != null) {
                            if (response.status == "success") {
                                employeeAdapter.removeEmployee(position)
                                toggleEmptyView()
                                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                                toggleEmptyView()

                            } else {
                                toggleEmptyView()
                                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(requireContext(), "Failed to delete employee", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            .setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onSearchClick(employee: AllEmployee) {
        val bundle = Bundle().apply {
            putParcelable("employee", employee)
        }

        val employeeDetailFragment = SearchEmp().apply {
            arguments = bundle
        }

        (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, employeeDetailFragment)
            //.addToBackStack(null)
            .commit()
    }
    private fun showAddEmployeesDialog() {
        toolBox.showConfirmationDialog(
            context = requireContext(),
            title = "No Employees are available",
            message = "Would you like to add a Employees now?",
            positiveButtonText = "YES",
            positiveButtonAction = { toolBox.fragmentChanger(AddNewEmployees(), requireContext()) },
            negativeButtonText = "NO",
            negativeButtonAction = { toolBox.fragmentChanger(DashBoard(), requireContext())},
            cancelable = false
        )
    }

}
