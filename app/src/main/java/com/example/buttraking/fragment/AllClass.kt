package com.example.buttraking.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buttraking.IDDetail.SchoolId
import com.example.buttraking.R
import com.example.buttraking.activity.MainActivity
import com.example.buttraking.adapter.AllClassAdapter
import com.example.buttraking.databinding.FragmentAllClassBinding
import com.example.buttraking.dataclass.ClassItem
import com.example.buttraking.helper.MethodLibrary
import com.example.buttraking.repository.AllClassRepository
import com.example.buttraking.viewmodel.AllClassViewModel
import com.example.buttraking.viewmodelfactory.AllClassViewModelFactory

class AllClass : Fragment() {
    private lateinit var binding: FragmentAllClassBinding
    private lateinit var allClassAdapter: AllClassAdapter
    private val classList = mutableListOf<ClassItem>()
    private lateinit var viewModel: AllClassViewModel
    val tooBox = MethodLibrary()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllClassBinding.inflate(inflater, container, false)
        val factory = AllClassViewModelFactory(AllClassRepository())
        viewModel = ViewModelProvider(this, factory).get(AllClassViewModel::class.java)
        if (!tooBox.isInternetAvailable(requireContext())){
            tooBox.dataFailedLoad(AllClass(),requireContext())
        }else{
            tooBox.startLoadingBar("Loading...", false, requireContext())
            initRecyclerView()
            observeData()
        }
        return binding.root
    }

    private fun initRecyclerView() {
        allClassAdapter = AllClassAdapter(classList, { classItem ->
          //  navigateToEditFragment(classItem)
        }, { classId, schoolId -> deleteClass(schoolId, classId) })

        binding.recyclerViewClasses.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewClasses.adapter = allClassAdapter
    }

    private fun observeData() {
        try {
            viewModel.getClasses(SchoolId().getSchoolId(requireContext())).observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    tooBox.stopLoadingBar()
                    response.data?.let {
                        classList.clear()
                        classList.addAll(it)
                        allClassAdapter.notifyDataSetChanged()
                    }
                } else {
                    tooBox.stopLoadingBar()
                    tooBox.showConfirmationDialog(
                        context = requireContext(),
                        title = "No classes are available",
                        message = "Would you like to add a class now?",
                        positiveButtonText = "YES",
                        positiveButtonAction = { tooBox.fragmentChanger(NewClass(), requireContext()) },
                        negativeButtonText = "NO",
                        negativeButtonAction = { tooBox.activityChanger(MainActivity(), requireContext()) },
                        cancelable = false)
//                Toast.makeText(requireContext(), "No data received", Toast.LENGTH_SHORT).show()
                }
            }
        }catch (e: Exception){
            tooBox.showConfirmationDialog(
                context = requireContext(),
                title = "Warning !",
                message = "Unknown Error found Please Contact with developer nurkude@gmail.com",
                positiveButtonText = "OK",
                positiveButtonAction = { tooBox.activityChanger(MainActivity(), requireContext()) },
                negativeButtonText = "",
                negativeButtonAction = { },
                cancelable = false)
        }
    }


/*
    private fun navigateToEditFragment(classItem: ClassItem) {
        tooBox.fragmentChanger(UpdateClass().apply {
            arguments = Bundle().apply {
                putSerializable("class_data", classItem)
            }
        }, requireContext())
    }

*/
    private fun deleteClass(schoolId: String, classId: String) {
        tooBox.showConfirmationDialog(
            context = requireContext(),
            title = "Class Deletion ! ",
            message = "Are you sure you want to delete this class?",
            positiveButtonText = "Yes",
            positiveButtonAction = {
                tooBox.startLoadingBar("Loading...",  false, requireContext())
              //  deleteClassAPICall(schoolId, classId)
                tooBox.fragmentChanger(AllClass(), requireContext())
                tooBox.stopLoadingBar()
            },
            negativeButtonText = "No",
            negativeButtonAction = { },
            cancelable = true )

    }
/*
    private fun deleteClassAPICall(schoolId: String, classId: String) {
        try {
            // Make the delete class API call
            viewModel.deleteClass(schoolId, classId, requireContext()).observe(viewLifecycleOwner) { isSuccess ->
                Toast.makeText(requireContext(), "Class deleted successfully", Toast.LENGTH_SHORT).show()
                if (isSuccess) {
                    Toast.makeText(requireContext(), "Class deleted successfully", Toast.LENGTH_SHORT).show()
                    // Find the class item to remove from the list
                    val classItem = classList.find { it.class_id == classId }
                    classItem?.let {
                        classList.remove(it)
                        Toast.makeText(requireContext(), "class delete", Toast.LENGTH_SHORT).show()
                        allClassAdapter.notifyDataSetChanged() // Notify the adapter
                        // Show success message only after deleting the class
                        Toast.makeText(requireContext(), "Class deleted successfully", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // If the deletion is unsuccessful, show failure message
                    Toast.makeText(requireContext(), "Failed to delete the class.", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            // Handle any unexpected errors
            e.printStackTrace()  // Log the exception for debugging purposes
            Toast.makeText(requireContext(), "Unknown error occurred while deleting the class.", Toast.LENGTH_SHORT).show()
        }
    }
*/


}






