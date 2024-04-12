package com.example.myapplicationmad

import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationmad.adapters.CountdownAdapter
import com.example.myapplicationmad.firestore.MedBox
import com.example.myapplicationmad.fragments.AddNewBoxFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class SpanOfOneBoxActivity : AppCompatActivity() {

    private lateinit var fabAddNewBox: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var countdownAdapter: CountdownAdapter
    private lateinit var fragmentContainer: FrameLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_span_of_one_box)

        recyclerView = findViewById(R.id.recyclerView3)
        fabAddNewBox = findViewById(R.id.AddNewBoxFAB)
        fragmentContainer = findViewById(R.id.fragmentAddNewBox)

        val homeButton = findViewById<ImageView>(R.id.homeButton3)
        val countdownButton = findViewById<ImageView>(R.id.countdownButton3)


        recyclerView.layoutManager = LinearLayoutManager(this)
        countdownAdapter = CountdownAdapter(emptyList()) {medication ->
            val fragment = AddNewBoxFragment.newInstance(medication.name)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentAddNewBox, fragment)
                .addToBackStack(null)
                .commit()

        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = countdownAdapter


        fabAddNewBox.setOnClickListener {
            fragmentContainer.visibility = View.VISIBLE


            val fragmentManager = supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentAddNewBox, AddNewBoxFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }


        homeButton.setOnClickListener {
            goToMainActivity()
        }

        countdownButton.setOnClickListener {
            goToSpanOfOneBoxActivity()
        }

        fetchDataFromYourDataSource()

    }



    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun goToSpanOfOneBoxActivity() {
        val intent = Intent(this, SpanOfOneBoxActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun fetchDataFromYourDataSource(): List<MedBox> {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()


        db.collection("medBoxes")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val medBoxes: MutableList<MedBox> = mutableListOf()

                for (document in documents) {
                    val medBox = document.toObject(MedBox::class.java)
                    medBoxes.add(medBox)
                }

                countdownAdapter.updateData(medBoxes)
            }
            .addOnFailureListener { exception ->
            }

        return emptyList()
    }

}