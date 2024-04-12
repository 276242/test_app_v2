package com.example.myapplicationmad.firestore

import com.example.myapplicationmad.MedSetUpActivity
import com.example.myapplicationmad.RegisterActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUserFS (activity: RegisterActivity, userInfo: User){

        mFireStore.collection("users")
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener{
            }
    }

    fun saveMedicationFS(activity: MedSetUpActivity, userId: String, medication: Medication) {
        val userMedicationsCollection = mFireStore.collection("users").document(userId).collection("medications")

        userMedicationsCollection.document(medication.name)
            .set(medication, SetOptions.merge())
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }
    }

}