package com.example.myapplicationmad.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationmad.R
import com.example.myapplicationmad.firestore.MedBox
import com.example.myapplicationmad.firestore.Medication
import java.util.Calendar
import java.util.Date


class CountdownAdapter(private var itemList: List<MedBox>, private val onItemClick: (Medication) -> Unit) :
    RecyclerView.Adapter<CountdownAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val medicineNameTextView: TextView = itemView.findViewById(R.id.txtMedName)
        var timeLeftTextView: TextView = itemView.findViewById(R.id.txtCountdown)
        val medicationImage: ImageView = itemView.findViewById(R.id.medicationImage)
        var progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        val additionalInfoTextView: TextView = itemView.findViewById(R.id.txtAdditionalInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_box_countdown, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]

        // data to view
        holder.medicineNameTextView.text = itemList[position].medName.toString()
        holder.medicationImage.setImageResource(R.drawable.icon_pill)
//        holder.progressBar = ProgressBar(holder.itemView.context, null, android.R.attr.progressBarStyleHorizontal)
//        holder.timeLeftTextView.text = itemList[position].daysTillNotification.toString()

        // calculating time left + update text
        val daysLeft = calculateDaysLeft(currentItem.startDate, currentItem.numbDoses)
        if (daysLeft == 1) {
            holder.timeLeftTextView.text = "Time Left: $daysLeft day"
        } else {
            holder.timeLeftTextView.text = "Time Left: $daysLeft days"
        }

        // progressbar
        val progress = calculateProgress(currentItem.startDate, currentItem.numbDoses)
        holder.progressBar.progress = progress


        holder.additionalInfoTextView.text = "Remember to refill your prescription before it ends!"
    }


    override fun getItemCount(): Int {
        return itemList.size
    }


    private fun calculateDaysLeft(startDate: Long, numbDoses: Int): Int {
        val currentDate = Calendar.getInstance().time

        // calculating end date
        val calendar = Calendar.getInstance()
        calendar.time = Date(startDate)
        calendar.add(Calendar.DAY_OF_YEAR, numbDoses)

        // caluclating the differnce, from milliseconds to days
        val daysLeft = ((calendar.timeInMillis - currentDate.time) / (24 * 60 * 60 * 1000)).toInt()

        return if (daysLeft >= 0) daysLeft else 0
    }



    private fun calculateProgress(startDate: Long, numbDoses: Int): Int {
        val remainingDoses = numbDoses - calculateDaysLeft(startDate, numbDoses)

        // progress as percenage
        return if (numbDoses > 0) ((remainingDoses.toDouble() / numbDoses.toDouble()) * 100).toInt() else 100
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItemList: List<MedBox>) {
        itemList = newItemList
        notifyDataSetChanged()
    }
}