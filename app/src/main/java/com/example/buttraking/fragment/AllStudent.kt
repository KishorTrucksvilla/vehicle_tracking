package com.example.buttraking.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.buttraking.IDDetail.SchoolId
import com.example.buttraking.R
import com.example.buttraking.activity.MainActivity
import com.example.buttraking.adapter.AllStudentAdapter
import com.example.buttraking.api.RetrofitHelper
import com.example.buttraking.databinding.FragmentAllStudentBinding
import com.example.buttraking.dataclass.Student
import com.example.buttraking.helper.MethodLibrary
import com.example.buttraking.repository.AllClassRepository
import com.example.buttraking.repository.getStudentRepository
import com.example.buttraking.viewmodel.AllClassViewModel
import com.example.buttraking.viewmodel.getStudentViewModel
import com.example.buttraking.viewmodelfactory.AllClassViewModelFactory
import com.example.buttraking.viewmodelfactory.getStudentViewModelFactory


class AllStudent : Fragment(), AllStudentAdapter.OnItemClickListener {

    private lateinit var binding: FragmentAllStudentBinding
    private lateinit var allStudentAdapter: AllStudentAdapter
    private var isDeleteProcessed = false
    private lateinit var viewModel: getStudentViewModel
    private lateinit var viewModelAllClass: AllClassViewModel
    val toolBox = MethodLibrary()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllStudentBinding.inflate(inflater, container, false)
        if (!toolBox.isInternetAvailable(requireContext())){
            toolBox.dataFailedLoad(AllStudent(),requireContext())
        }else{
            setupViewModel()
            setupRecyclerView()
            toolBox.startLoadingBar("Loading...", false, requireContext())

            viewModel.fetchStudentsBySchoolId(SchoolId().getSchoolId(requireContext()))
            viewModelAllClass()
            toolBox.clicked(binding.addStudentIconLayout) {toolBox.fragmentChanger(AddStudent(),requireContext())}

            binding.imgAddStudent.setOnClickListener{toolBox.fragmentChanger(AddStudent(), requireContext()) }

        }
        return binding.root
    }

    private fun viewModelAllClass() {
        val repository = AllClassRepository()
        val factory = AllClassViewModelFactory(repository)
        viewModelAllClass = ViewModelProvider(this, factory).get(AllClassViewModel::class.java)

    }


    private fun setupViewModel() {
        val factory = getStudentViewModelFactory(getStudentRepository(RetrofitHelper.getApiService()))
        viewModel = ViewModelProvider(this, factory).get(getStudentViewModel::class.java)

        viewModel.students.observe(viewLifecycleOwner) { students ->
            toolBox.stopLoadingBar()
            //  Toast.makeText(requireContext(), students.toString(), Toast.LENGTH_SHORT).show()
            allStudentAdapter.updateStudents(students)
            binding.addStudentIconLayout.visibility = View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            toolBox.stopLoadingBar()
//            toolBox.showConfirmationDialog(
//                context = requireContext(),
//                title = "Student are not Available !",
//                message = "Would you like to add student.",
//                positiveButtonText = "yes",
//                positiveButtonAction = { toolBox.fragmentChanger(AddStudent(), requireContext()) },
//                negativeButtonText = "no",
//                negativeButtonAction = {toolBox.activityChanger(MainActivity(), requireContext()) },
//                cancelable = false
//            )
            toolBox.showConfirmationDialog(
                context = requireContext(),
                title = "No student are available.",
                message = "Would you like to add a student now?",
                positiveButtonText = "yes",
                positiveButtonAction = { toolBox.fragmentChanger(AddStudent(), requireContext()) },
                negativeButtonText = "no",
                negativeButtonAction = { toolBox.activityChanger(MainActivity(), requireContext()) },
                cancelable = false)
        }
    }

    private fun setupRecyclerView() {
        allStudentAdapter = AllStudentAdapter(mutableListOf(), this)
        binding.recyclerViewAllStudent.layoutManager = GridLayoutManager(activity, 2)
        binding.recyclerViewAllStudent.adapter = allStudentAdapter

        binding.searchViewStudent.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("SearchView", "Search query changed: $newText")
                allStudentAdapter.filter(newText.orEmpty())
                return true
            }
        })

        binding.searchViewStudent.setOnClickListener {
            binding.searchViewStudent.isFocusable = true
            binding.searchViewStudent.isIconified = false
            binding.searchViewStudent.requestFocus()
        }
    }

    override fun onDeleteClick(student: Student, position: Int) {
        val schoolId = student.school_id.trim()
        val studentId = student.id

        toolBox.showConfirmationDialog(
            context = requireContext(),
            title = "Delete Student !",
            message = "Are you sure you want to delete ${student.st_name}?",
            positiveButtonText = "Yes",
            positiveButtonAction = {
                toolBox.startLoadingBar("Loading...",false, requireContext())
                viewModel.deleteStudent(schoolId, studentId)

                viewModel.deleteStatus.observe(viewLifecycleOwner) { success ->
                    if (!isDeleteProcessed) {
                        isDeleteProcessed = true
                        if (success) {
                            toolBox.stopLoadingBar()
                            toolBox.replaceFragment(AllStudent(), requireContext())
//                        toolBox.showConfirmationDialog(
//                            context = requireContext(),
//                            title = "Student Deleted Successfully",
//                            positiveButtonText = "OK",
//                            positiveButtonAction = {  },
//                            negativeButtonText = "",
//                            negativeButtonAction = { },
//                            cancelable = true )
                            allStudentAdapter.removeStudent(position)
                            Log.d("onDeleteClick", "Student deleted successfully at position $position")
                        } else {
                            Log.e("onDeleteClick", "Failed to delete student")
                            Toast.makeText(context, "Failed to delete student", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            },
            negativeButtonText = "No",
            negativeButtonAction = { },
            cancelable = true )
    }

    override fun onEditClick(student: Student) {
        toolBox.fragmentChanger(EditStudent().apply {
            arguments = Bundle().apply { putParcelable("student", student)
                putString("Student_id", student.id)
            }
        } ,requireContext())
    }

    override fun onSearchClick(student: Student) {
        toolBox.fragmentChanger(SearchStudent().apply {
            arguments = Bundle().apply { putParcelable("student", student) }
        } , requireContext())

    }
}
