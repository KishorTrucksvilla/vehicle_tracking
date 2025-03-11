package com.example.buttraking.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.buttraking.IDDetail.SchoolId
import com.example.buttraking.databinding.ItemAllclassBinding
import com.example.buttraking.dataclass.ClassItem
import com.example.buttraking.helper.MethodLibrary
import com.example.buttraking.utils.CircularProgressViewTotalStudents

class AllClassAdapter(
    private val classList: MutableList<ClassItem>,
    private val onEditClick: (ClassItem) -> Unit, // Callback for edit
    private val onDeleteClick: (String, String) -> Unit, // Callback for delete

) : RecyclerView.Adapter<AllClassAdapter.ClassViewHolder>() {


    inner class ClassViewHolder(private val binding: ItemAllclassBinding) :
        RecyclerView.ViewHolder(binding.root) {
//        private lateinit var circularProgressViewTotalStudents: CircularProgressViewTotalStudents
//         var boyPercentage : Int = 10
//            private var boyPercentage: Int = 0

        fun bind(classItem: ClassItem) {


//            MethodLibrary().circlePrint(boyPercentage.toString() ,binding.circularProgressViewTotalBoys, binding.root.context)

            binding.tvClassName.text = classItem.class_name
            binding.idtest.text = classItem.class_id
            // setupPieChart(classItem)

            // Set edit icon click listener
            binding.iconClassEdit.setOnClickListener {
                onEditClick(classItem)
            }

            // Set delete icon click listener
            binding.iconDelete.setOnClickListener {
                if (!classItem.class_id.isNullOrEmpty()) {
                    onDeleteClick(classItem.class_id, SchoolId().getSchoolId(binding.root.context))
                } else {
                    Log.e("AllClassAdapter", "Class ID is null or empty")
                    Toast.makeText(
                        itemView.context,
                        "Class ID is null or empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


       /* private fun setupPieChart(classItem: ClassItem) {
            try {
                val entries = ArrayList<PieEntry>()
                if (classItem.total_boys.toInt() > 0) entries.add(PieEntry(classItem.total_boys.toFloat(), "Boys"))
                if (classItem.total_girls.toInt() > 0) entries.add(PieEntry(classItem.total_girls.toFloat(), "Girls"))
                val totalStudents = classItem.total_boys.toInt() + classItem.total_girls.toInt()
                binding.tvClassDescription.text = totalStudents.toString()
                binding.tvBoys.text = classItem.total_boys
                binding.tvGirls.text = classItem.total_girls

                val total = classItem.total_boys.toInt() + classItem.total_girls.toInt()
                val boys = (classItem.total_boys.toInt() * 100 ) / total
                CircularProgressViewTotalStudents(binding.root.context).setCircleName("Boys")
                val girls = (classItem.total_girls.toInt() * 100 ) / total

                MethodLibrary().circlePrint(boys.toString() ,binding.circularProgressViewTotalBoys, binding.root.context)
                MethodLibrary().circlePrint(girls.toString() ,binding.circularProgressViewTotalGirls, binding.root.context)

                val dataSet = PieDataSet(entries, "Students")
                dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
            }catch (e: Exception){
//                Toast.makeText(binding.root.context, e.message, Toast.LENGTH_SHORT).show()
            }

        }
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val binding = ItemAllclassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClassViewHolder(binding)


    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        holder.bind(classList[position])
    }

    override fun getItemCount(): Int = classList.size
}
