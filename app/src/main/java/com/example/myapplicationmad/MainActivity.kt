package com.example.myapplicationmad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationmad.adapters.MedicationAdapter
import com.example.myapplicationmad.firestore.Medication
import com.example.myapplicationmad.fragments.ConfirmMedicationFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddMedication: FloatingActionButton
    private lateinit var medicationAdapter: MedicationAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        fabAddMedication = findViewById(R.id.addNewMedicationFAB)
        val homeButton = findViewById<ImageView>(R.id.homeButton)
        val countdownButton = findViewById<ImageView>(R.id.countdownButton)


        medicationAdapter = MedicationAdapter(emptyList()) { medication ->
            val fragment = ConfirmMedicationFragment.newInstance(medication.name)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerNotif, fragment)
                .addToBackStack(null)
                .commit()
        }


        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = medicationAdapter

        fabAddMedication.setOnClickListener {
            val intent = Intent(this, MedSetUpActivity::class.java)
            startActivity(intent)
        }

        homeButton.setOnClickListener {
            goToMainActivity()
        }

        countdownButton.setOnClickListener {
            goToSpanOfOneBoxActivity()
        }

        fetchDataFromFireStore()

    }


    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToSpanOfOneBoxActivity() {
        val intent = Intent(this, SpanOfOneBoxActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun fetchDataFromFireStore() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        if (userId != null) {
            db.collection("users")
                .document(userId)
                .collection("medications")
                .get()
                .addOnSuccessListener { documents ->
                    val medications = mutableListOf<Medication>()
                    for (document in documents) {
                        val name = document.id
                        val dosage = document.getString("dosage") ?: ""
                        medications.add(Medication(name, dosage))
                    }
                    medicationAdapter.updateData(medications)
                }
                .addOnFailureListener { exception ->
                }
        }
    }
}