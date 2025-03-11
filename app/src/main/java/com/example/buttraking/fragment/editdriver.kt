package com.example.buttraking.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buttraking.IDDetail.SchoolId
import com.example.buttraking.R
import com.example.buttraking.api.RetrofitHelper
import com.example.buttraking.databinding.FragmentEditdriverBinding
import com.example.buttraking.dataclass.VehicleDataAdmin
import com.example.buttraking.helper.MethodLibrary
import com.example.buttraking.repository.PutDriverRepository
import com.example.buttraking.viewmodel.PutDriverViewModel
import com.example.buttraking.viewmodelfactory.PutDriverViewModelFactory

class editdriver : Fragment() {
    private lateinit var binding: FragmentEditdriverBinding
    private lateinit var viewModel: PutDriverViewModel
    var toolBox = MethodLibrary()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentEditdriverBinding.bind(inflater.inflate(R.layout.fragment_editdriver,null))
        arguments?.getParcelable<VehicleDataAdmin>("driver")?.let { emp ->
            populateFields(emp)
        }
        setupLisners()
        return binding.root
    }
    private fun populateFields(emp: VehicleDataAdmin) {
        binding.etDriverName.setText(emp.driver_name)
        //  binding.etregistrationNo.setText(emp.re)
        // binding.etDriverType.setText(emp.driver_type)
        // binding.etStatus.setText(emp.st_status)

        binding.etNumber.setText(emp.number)
        binding.txtid.setText(emp.id)
        binding.edtvehoclenumber.setText(emp.vehicle_number)

        setSpinnerSelection(binding.etDriverType, emp.driver_type)

    }
    private fun setSpinnerSelection(spinner: Spinner, value: String?) {
        val adapter = spinner.adapter
        if (adapter != null && value != null) {
            for (i in 0 until adapter.count) {
                if (adapter.getItem(i).toString() == value) {
                    spinner.setSelection(i)
                    break
                }
            }
        }
    }
    private fun setupLisners(){
        binding.btnSubmit.setOnClickListener{
            setviewmodel()
            addDriver()
        }

    }
    private fun setviewmodel(){
        val apiService= RetrofitHelper.getApiService()
        val  repository= PutDriverRepository(apiService)
        val factory= PutDriverViewModelFactory(repository)
        viewModel= ViewModelProvider(this, factory).get(PutDriverViewModel::class.java)

    }


    private fun addDriver() {
        // Assuming you have EditText fields in your layout, get the input values
        val driverName = binding.etDriverName.text.toString()
        val driverType = binding.etDriverType.selectedItem.toString()
        val number = binding.etNumber.text.toString()
        val vehiclenumber = binding.edtvehoclenumber.text.toString()
        val ID = binding.txtid.text.toString()


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
            "school_id" to schoolId,
            "id" to ID

        )

        // Call the ViewModel method to submit the driver data
        viewModel.putDriver(driverData)

        // Observe the LiveData to get the response
        viewModel.putDriverResponse.observe(viewLifecycleOwner) { response ->
            if (response.status == "success") {
                showPreviousDataDialog()
                //clearFields()
            } else {
                // showMessage("Failed: ${response.message}")
            }
        }
    }
    private fun clearFields() {
        binding.etDriverName.text?.clear()
        binding.etNumber.text?.clear()
        binding.edtvehoclenumber.text?.clear()
        binding.txtid.text = ""  // If txtid is a TextView, use `.text = ""`
    }

    private fun showMessage(message: String) {
        // Display a message (e.g., using a Toast or Snackbar)
        // For example, use Toast:
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    private fun showPreviousDataDialog() {
        toolBox.showConfirmationDialog(
            context = requireContext(),
            title = "Driver Updated Successfully",
            message = "Would you like to Updated more Driver?",
            positiveButtonText = "YES",
            positiveButtonAction = { toolBox.fragmentChanger(editdriver(), requireContext()) },
            negativeButtonText = "NO",
            negativeButtonAction = { toolBox.fragmentChanger(AllDriver(), requireContext()) },
            cancelable = false
        )
    }
}

