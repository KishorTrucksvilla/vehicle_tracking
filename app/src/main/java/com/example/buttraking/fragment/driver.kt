package com.example.buttraking.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buttraking.IDDetail.SchoolId
import com.example.buttraking.R
import com.example.buttraking.api.RetrofitHelper
import com.example.buttraking.databinding.FragmentDriverBinding
import com.example.buttraking.helper.MethodLibrary
import com.example.buttraking.repository.AddDriverRepository
import com.example.buttraking.viewmodel.AddDriverViewModel
import com.example.buttraking.viewmodelfactory.AddDriverViewModelFactory

class driver : Fragment() {
    private lateinit var binding: FragmentDriverBinding
    private lateinit var viewModel: AddDriverViewModel

    var toolBox = MethodLibrary()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDriverBinding.bind(inflater.inflate(R.layout.fragment_driver, null))
        Setupviewmodel()
        setupLisners()
        return binding.root
    }

    private fun Setupviewmodel() {
        val apiService = RetrofitHelper.getApiService()
        val repository = AddDriverRepository(apiService)
        val factory = AddDriverViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(AddDriverViewModel::class.java)

    }

    private fun setupLisners() {
        binding.btnSubmit.setOnClickListener {
            addDriver()
        }

    }

    private fun addDriver() {
        // Assuming you have EditText fields in your layout, get the input values
        val driverName = binding.etDriverName.text.toString()
        val driverType = binding.etDriverType.selectedItem.toString()
        val number = binding.etNumber.text.toString()
        val vehiclenumber = binding.edtvehoclenumber.text.toString()

        /* val latitude = binding.etLatitude.text.toString()
         val longitude = binding.etLongitude.text.toString()
      */ //  val username = binding.etUsername.text.toString()
        val schoolId = SchoolId().getSchoolId(requireContext())

        val password =
            if (binding.etDriverName.text.toString().length >= 3 && number.length >= 7) {
                binding.etDriverName.text.toString().trim()
                    .substring(0, 3) + number.substring(6)
            } else {
                "defaultPassword"
            }
        // Put these values into the DriverData map
        val driverData = mapOf(
            "driver_name" to driverName,
            "driver_type" to driverType,
            "number" to number,
            /*  "latitude" to latitude,
            "longitude" to longitude,*/
            "username" to number,
            "password" to password,
            "vehicle_number" to vehiclenumber,
            "school_id" to schoolId
        )

        // Call the ViewModel method to submit the driver data
        viewModel.submitAddDriver(driverData)

        // Observe the LiveData to get the response
        viewModel.AddDriverResponse.observe(viewLifecycleOwner) { response ->
            if (response?.status == true) {
                // Handle success (e.g., show a success message or navigate to another screen)
                showPreviousDataDialog()
                clearFields()
            } else {
                // Handle failure (e.g., show an error message)
                showMessage("Failed to add driver: ${response?.message}")
            }
        }
    }
    private fun clearFields() {
        binding.etDriverName.text?.clear()
        binding.etNumber.text?.clear()
        binding.edtvehoclenumber.text?.clear()
    }
    private fun showMessage(message: String) {
        // Display a message (e.g., using a Toast or Snackbar)
        // For example, use Toast:
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showPreviousDataDialog() {

        toolBox.showConfirmationDialog(
            context = requireContext(),
            title = "Driver Added Successfully",
            message = "Would you like to add more Driver?",
            positiveButtonText = "YES",
            positiveButtonAction = { toolBox.fragmentChanger(driver(), requireContext()) },
            negativeButtonText = "NO",
            negativeButtonAction = { toolBox.fragmentChanger(AllDriver(), requireContext()) },
            cancelable = false
        )
    }
}
