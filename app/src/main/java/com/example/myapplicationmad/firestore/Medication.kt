package com.example.myapplicationmad.firestore

data class Medication(
   // val userId: String = "",
    val name: String = "",
    val dosage: String = "",
    val notes: String = "",
    val notifications: Notification? = null
)

data class Notification(
    val hour: Int = -1,
    val minute: Int = -1
)

data class MedBox(
    val medName: Medication? = null,
    val ifNotifs: Boolean = false,
    val daysBefore: Int = 0,
    val daysTillNotification: Int = 0,
    val startDate: Long = 0,
    val numbDoses: Int = 0,
)