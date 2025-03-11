package com.example.buttraking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.buttraking.R
import com.example.buttraking.databinding.ItemAllemployeeBinding
import com.example.buttraking.dataclass.VehicleDataAdmin
import com.example.buttraking.helper.MethodLibrary

class AllDriverAdapter(
    private var driverList: MutableList<VehicleDataAdmin>,
    private val listener: OnDriverActionListener
) : RecyclerView.Adapter<AllDriverAdapter.DriverViewHolder>(), Filterable {

    private var fullDriverList: MutableList<VehicleDataAdmin> = ArrayList(driverList)

    interface OnDriverActionListener {
        fun onEditDriver(driver: VehicleDataAdmin)
        fun onDeleteDriver(driver: VehicleDataAdmin, position: Int)
        fun onSearchClick(driver: VehicleDataAdmin)
    }

    inner class DriverViewHolder(private val binding: ItemAllemployeeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(driver: VehicleDataAdmin) {
            binding.tvEmployeeName.text = driver.driver_name
            binding.tvEmployeeTitle.text = driver.driver_type

           /* MethodLibrary().displayImage(
                "${ImageUtil.BASE_URL_IMAGE}driver/${driver.id}.jpg",
                binding.imageViewEmployee,
                binding.root.context
            )*/

            binding.imageViewEmployee.setImageResource(
                when (driver.role) {
                    "Female" -> R.drawable.femaleemployee
                    "Male" -> R.drawable.maleemployee
                    else -> R.drawable.maleemployee
                }
            )
            binding.gender.text = driver.role ?: "Unspecified"

            binding.iconEdit.setOnClickListener { listener.onEditDriver(driver) }
            binding.iconDelete.setOnClickListener { listener.onDeleteDriver(driver, adapterPosition) }
            binding.iconSearch.setOnClickListener { listener.onSearchClick(driver) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverViewHolder {
        val binding = ItemAllemployeeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DriverViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DriverViewHolder, position: Int) {
        holder.bind(driverList[position])
    }

    override fun getItemCount(): Int = driverList.size

    fun removeDriver(position: Int) {
        if (position in driverList.indices) {
            driverList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun updateList(newList: List<VehicleDataAdmin>) {
        driverList.clear()
        driverList.addAll(newList)
        fullDriverList = ArrayList(newList) // Keep backup for search
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = if (constraint.isNullOrEmpty()) {
                    fullDriverList
                } else {
                    val filterPattern = constraint.toString().trim().lowercase()
                    fullDriverList.filter {
                        it.driver_name.lowercase().contains(filterPattern) ||
                                it.driver_type.lowercase().contains(filterPattern)
                    }.toMutableList()
                }
                return FilterResults().apply { values = filteredList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                driverList.clear()
                driverList.addAll(results?.values as MutableList<VehicleDataAdmin>)
                notifyDataSetChanged()
            }
        }
    }
}
