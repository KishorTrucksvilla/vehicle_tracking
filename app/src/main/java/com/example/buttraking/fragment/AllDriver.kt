package com.example.buttraking.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.buttraking.IDDetail.SchoolId
import com.example.buttraking.R
import com.example.buttraking.adapter.AllDriverAdapter
import com.example.buttraking.api.RetrofitHelper
import com.example.buttraking.databinding.FragmentAllDriverBinding
import com.example.buttraking.dataclass.VehicleDataAdmin
import com.example.buttraking.repository.GetAdmindataRepository
import com.example.buttraking.viewmodel.GetAdmindataViewModel
import com.example.buttraking.viewmodelfactory.ViewModelFactoryAdmindata

class AllDriver : Fragment(), AllDriverAdapter.OnDriverActionListener {
    private lateinit var binding: FragmentAllDriverBinding
    private lateinit var viewModel: GetAdmindataViewModel
    private lateinit var allDriverAdapter: AllDriverAdapter
    private val driverList = mutableListOf<VehicleDataAdmin>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllDriverBinding.inflate(inflater, container, false)
        setupRecyclerView()
        setupSearchView()
        setupViewModel()
        return binding.root
    }

    private fun setupViewModel() {
        val apiService = RetrofitHelper.getApiService()
        val repository = GetAdmindataRepository(apiService)
        val factory = ViewModelFactoryAdmindata(repository)
        viewModel = ViewModelProvider(this, factory).get(GetAdmindataViewModel::class.java)

        val schoolId = SchoolId().getSchoolId(requireContext()).trim()
        Log.d("ViewModelSetup", "Fetching data for School ID: $schoolId")

        viewModel.fetcAdmindata(schoolId)
        observeData()
    }

    private fun observeData() {
        viewModel.Adminrdata.observe(viewLifecycleOwner, Observer { response ->
            if (response?.status == true && !response.data.isNullOrEmpty()) {
                Log.d("LatLongData", "Received ${response.data.size} drivers")
                allDriverAdapter.updateList(response.data) // Call updateList here
            } else {
                Log.e("LatLongData", "No data received or status is false")
            }
        })
    }


    private fun setupRecyclerView() {
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewAllEmployees.apply {
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
            allDriverAdapter = AllDriverAdapter(driverList, this@AllDriver) // Correct reference to Fragment
            adapter = allDriverAdapter
        }
    }

    private fun setupSearchView() {
        binding.searchViewEmployees.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                allDriverAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                allDriverAdapter.filter.filter(newText)
                return false
            }
        })
    }


    override fun onEditDriver(driver: VehicleDataAdmin) {
        val bundle = Bundle().apply {
            putParcelable("driver", driver)
        }

        val employeeDetailFragment = editdriver().apply {
            arguments = bundle
        }

        (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, employeeDetailFragment)
            //.addToBackStack(null)
            .commit()
    }

    override fun onDeleteDriver(driver: VehicleDataAdmin, position: Int) {
        val schoolId = driver.school_id.trim()
        val employeeId = driver.id
        val employeeName = driver.driver_name

        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Delete $employeeName")
            .setMessage("Are you sure you want to delete employee?")
            .setPositiveButton("Yes") { dialog, which ->
                Log.d("DeleteEmployee", "Attempting to delete employee with ID: $employeeId, School ID: $schoolId")

                viewModel.deletedriver(schoolId, employeeId)
                    .observe(viewLifecycleOwner) { response ->
                        if (response != null) {
                            if (response.status == "success") {
                                allDriverAdapter.removeDriver(position)
                                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()

                            } else {
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
    override fun onSearchClick(driver: VehicleDataAdmin) {
        val bundle = Bundle().apply {
            putParcelable("driver", driver)
        }

        val employeeDetailFragment = DriverDetail().apply {
            arguments = bundle
        }

        (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, employeeDetailFragment)
            //.addToBackStack(null)
            .commit()    }
}
