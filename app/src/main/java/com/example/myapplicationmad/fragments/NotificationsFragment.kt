package com.example.myapplicationmad.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TimePicker
import androidx.fragment.app.FragmentActivity
import com.example.myapplicationmad.MainActivity
import com.example.myapplicationmad.NotificationReceiver
import com.example.myapplicationmad.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class NotificationsFragment : Fragment() {

    private lateinit var timePicker: TimePicker
    private lateinit var saveButton: Button
    private lateinit var backButton: ImageButton

    private val mFireStore = FirebaseFirestore.getInstance()
    private var userId = ""
    private var medicationId = ""
    private var medicationName = "medicationName"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_notifications, container, false)

        timePicker = rootView.findViewById(R.id.timePicker)
        saveButton = rootView.findViewById(R.id.saveButton)
        backButton = rootView.findViewById<ImageButton>(R.id.backButton)


        backButton.setOnClickListener {
            val activity: FragmentActivity? = activity
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }


        saveButton.setOnClickListener {
//            saveSelectedTime(userId, medicationId, medicationName)
//            // from https://stackoverflow.com/a/39942706
//            startActivity(Intent(activity, MainActivity::class.java))
            fetchUserAndMedicationInfo()
        }

        return rootView
    }

    private fun fetchUserAndMedicationInfo() {
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        medicationId = arguments?.getString("medicationId") ?: ""

        if (userId.isNotEmpty() && medicationId.isNotEmpty()) {
            saveSelectedTime(userId, medicationId, medicationName)
            startActivity(Intent(activity, MainActivity::class.java))
        }
    }

    private fun saveSelectedTime(userId: String, medicationId: String, medicationName: String) {
        val hour = timePicker.hour
        val minute = timePicker.minute

        saveTimeToDatabase(userId, medicationId, hour, minute)
        scheduleDailyNotification(hour, minute, medicationId)
    }


    private fun saveTimeToDatabase(userId: String, medicationId: String, hour: Int, minute: Int) {
        val notificationsRef = mFireStore
            .collection("users")
            .document(userId)
            .collection("medications")
            .document(medicationId)
            .collection("notifications")
            .document("notification")

        val notificationData = mapOf("hour" to hour, "minute" to minute)

        notificationsRef.set(notificationData)
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }
    }



    // partly from https://developer.android.com/reference/android/app/TimePickerDialog#TimePickerDialog(android.content.Context,%20android.app.TimePickerDialog.OnTimeSetListener,%20int,%20int,%20boolean)
    private fun scheduleDailyNotification(hour: Int, minute: Int, medicationId: String) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.action = "MEDICATION_REMINDER_ACTION"
        intent.putExtra("medicationId", medicationId)

        val requestCode = medicationId.hashCode()

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        // repeat every day
        alarmManager?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}