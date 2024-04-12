package com.example.myapplicationmad.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationmad.R
import com.example.myapplicationmad.firestore.Medication

class MedicationAdapter(private var itemList: List<Medication>, private val onItemClick: (Medication) -> Unit) :
    RecyclerView.Adapter<MedicationAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val medicationNameTextView: TextView = itemView.findViewById(R.id.medicationName)
        val medicationDosageTextView: TextView = itemView.findViewById(R.id.medicationDosagetv)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medication, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]

        holder.medicationNameTextView.text = itemList[position].name
        holder.medicationDosageTextView.text = itemList[position].dosage
        itemList[position]


        holder.itemView.setOnClickListener {
            onItemClick(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }


    fun updateData(newMedications: List<Medication>) {
        itemList = newMedications
        notifyDataSetChanged()
    }
}