package com.example.buttraking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.buttraking.databinding.ItemAllStudentBinding
import com.example.buttraking.dataclass.Student
import com.example.buttraking.helper.MethodLibrary

class AllStudentAdapter(
    private val studentList: MutableList<Student>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<AllStudentAdapter.StudentViewHolder>() {

    private var studentNumber: Int = 0

    private var filteredList: MutableList<Student> = mutableListOf()

    init {
        filteredList.addAll(studentList)
    }

    interface OnItemClickListener {
        fun onDeleteClick(student: Student, position: Int)
        fun onEditClick(student: Student)
        fun onSearchClick(student: Student)
    }

    inner class StudentViewHolder(private val binding: ItemAllStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(student: Student, position: Int) {
            binding.tvEmployeeName.text = student.st_name
            binding.tvRollNumber.text = student.registration_no
            binding.tvScchool.text = student.class_name
            binding.tvEmployeeName.text = student.st_name
           // MethodLibrary().displayImage("${ImageUtil.BASE_URL_IMAGE}student/${student.picture}", binding.imageViewEmployee, binding.root.context)
            studentNumber++
            binding.tvcount.setText(studentNumber.toString())

            when(student.gender){
                "Male" -> binding.imageViewEmployee.setImageResource(com.example.buttraking.R.drawable.malestudent)
                "Female" -> binding.imageViewEmployee.setImageResource(com.example.buttraking.R.drawable.femalestudent)
            }

            binding.iconDelete.setOnClickListener {
                listener.onDeleteClick(student, position)
            }

            binding.iconEdit.setOnClickListener {
                listener.onEditClick(student)
            }
            binding.iconSearch.setOnClickListener {
                listener.onSearchClick(student)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ItemAllStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(filteredList[position], position)
    }

    override fun getItemCount(): Int = filteredList.size

    fun updateStudents(newStudents: List<Student>) {
        studentList.clear()
        studentList.addAll(newStudents)
        filter("")
    }

    fun filter(query: String) {
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(studentList)
        } else {
            filteredList.addAll(studentList.filter {
                it.st_name.contains(query, ignoreCase = true)
            })
        }
        //  Log.d("SearchFilter", "Query: \"$query\", Filtered size: ${filteredList.size}")
        notifyDataSetChanged()
    }

    fun removeStudent(position: Int) {
        if (position in filteredList.indices) {
            val studentToRemove = filteredList[position]
            studentList.remove(studentToRemove)
            filteredList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, filteredList.size)
        }
    }
}
