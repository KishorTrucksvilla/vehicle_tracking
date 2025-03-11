package com.example.buttraking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.buttraking.R
import com.example.buttraking.databinding.ItemAllemployeeBinding
import com.example.buttraking.dataclass.AllEmployee

class AllEmployeeAdapter(
    private var employeeList: MutableList<AllEmployee>,
    private val listener: OnEmployeeActionListener
) : RecyclerView.Adapter<AllEmployeeAdapter.EmployeeViewHolder>() {

    private var filteredList: MutableList<AllEmployee> = mutableListOf()

    init {
        filteredList.addAll(employeeList)
    }

    interface OnEmployeeActionListener {
        fun onEditEmployee(employee: AllEmployee)
        fun onDeleteEmployee(employee: AllEmployee, position: Int)
        fun onSearchClick(employee: AllEmployee)
    }

    inner class EmployeeViewHolder(private val binding: ItemAllemployeeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(employee: AllEmployee) {
            binding.imageViewEmployee.setImageResource(employee.img)
            binding.tvEmployeeName.text = employee.name
            binding.tvEmployeeTitle.text = employee.title

           // MethodLibrary().displayImage("${ImageUtil.BASE_URL_IMAGE}employee/${employee.picture}", binding.imageViewEmployee , binding.root.context)

            when (employee.gender) {
                "Female" -> {
                    binding.imageViewEmployee.setImageResource(R.drawable.femaleemployee)
                    binding.gender.text = "Female"
                }
                "Male" -> {
                    binding.imageViewEmployee.setImageResource(R.drawable.maleemployee)
                    binding.gender.text = "Male"
                }
                "Other" -> {
                    binding.imageViewEmployee.setImageResource(R.drawable.maleemployee)
                    binding.gender.text = "Other"
                }
                else -> {
                    binding.imageViewEmployee.setImageResource(R.drawable.maleemployee)
                    binding.gender.text = "Unspecified"
                }
            }



            binding.iconEdit.setOnClickListener {
                listener.onEditEmployee(employee)
            }
            binding.iconDelete.setOnClickListener {
                listener.onDeleteEmployee(employee, adapterPosition)
            }
            binding.iconSearch.setOnClickListener {
                listener.onSearchClick(employee)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val binding = ItemAllemployeeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmployeeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val employee = filteredList[position] // Use filteredList for displaying data
        holder.bind(employee)
    }

    override fun getItemCount(): Int {
        return filteredList.size // Return the size of filteredList (filtered or full list)
    }

    // Filtering method
    fun filter(query: String) {
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(employeeList) // Show all employees if query is empty
        } else {
            filteredList.addAll(employeeList.filter {
                it.employee_name.contains(query, ignoreCase = true) || it.employee_role.contains(query, ignoreCase = true)
            })
        }
        notifyDataSetChanged() // Refresh the adapter after filtering
    }

    fun removeEmployee(position: Int) {
        if (position in filteredList.indices) {
            val employeeToRemove = filteredList[position]
            employeeList.remove(employeeToRemove)
            filteredList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, filteredList.size)
        }
    }

    // Update the employee list and refresh the adapter
    fun updateList(newList: List<AllEmployee>) {
        this.employeeList = newList.toMutableList()
        filteredList.clear()
        filteredList.addAll(employeeList)
        notifyDataSetChanged()


    }
}
