package com.example.myapplicationmad.firestore

import java.util.*

data class MedicationTaken(
    val medicationId: String = "",
    val userId: String = "",
    val dateTime: Date = Calendar.getInstance().time,
    val isTaken: Boolean = false
)