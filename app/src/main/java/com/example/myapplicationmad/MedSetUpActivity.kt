package com.example.myapplicationmad

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import com.example.myapplicationmad.firestore.FireStoreClass
import com.example.myapplicationmad.firestore.Medication
import com.example.myapplicationmad.fragments.NotificationsFragment
import com.google.firebase.auth.FirebaseAuth

class MedSetUpActivity : BaseActivity() {

    private lateinit var inputMedName: EditText
    private lateinit var inputDosage: EditText
    private lateinit var unitSpinner: Spinner
    private lateinit var inputNotes: EditText
    private lateinit var setUpNotifButton: Button
    private lateinit var fragmentContainer: FragmentContainerView
    private lateinit var textMedication: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_med_set_up)

        inputMedName = findViewById(R.id.editMedicationName)
        inputDosage = findViewById(R.id.editDosage)
        unitSpinner = findViewById(R.id.unitSpinner)
        inputNotes = findViewById(R.id.editNotes)
        setUpNotifButton = findViewById(R.id.setUpNotifButton)
        fragmentContainer = findViewById(R.id.fragmentNotifications)
        textMedication = findViewById(R.id.textViewMedication)

        val homeButton = findViewById<ImageView>(R.id.homeButton2)
        val countdownButton = findViewById<ImageView>(R.id.countdownButton2)

        // modified from https://developer.android.com/develop/ui/views/components/spinner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.units_array,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        unitSpinner.adapter = adapter



        setUpNotifButton.setOnClickListener {
            if (validateInputs()) {
                val userId = (FirebaseAuth.getInstance().currentUser)?.uid ?: ""

                val medication = Medication(
//                    userId = userId,
                    name = inputMedName.text.toString(),
                    dosage = "${inputDosage.text} ${unitSpinner.selectedItem}",
                    notes = inputNotes.text.toString(),
                    notifications = null
                )

                FireStoreClass().saveMedicationFS(this@MedSetUpActivity, userId, medication)

                inputMedName.isEnabled = false
                inputMedName.visibility = View.GONE
                inputDosage.isEnabled = false
                inputDosage.visibility = View.GONE
                unitSpinner.isEnabled = false
                unitSpinner.visibility = View.GONE
                inputNotes.isEnabled = false
                inputNotes.visibility = View.GONE
                setUpNotifButton.isEnabled = false
                setUpNotifButton.visibility = View.GONE
                textMedication.visibility = View.GONE

                fragmentContainer.visibility = View.VISIBLE
                val fragmentManager = supportFragmentManager
                val transaction = fragmentManager.beginTransaction()

                val fragment = NotificationsFragment().apply {
                    arguments = Bundle().apply {
                        putString("medicationId", medication.name)
                    }
                }

                transaction.add(R.id.fragmentNotifications, fragment)
                transaction.addToBackStack(null).commit()

            }
        }

        homeButton.setOnClickListener {
            goToMainActivity()
        }

        countdownButton.setOnClickListener {
            goToSpanOfOneBoxActivity()
        }

    }

//    private fun showNotificationsFragment() {
//        val fragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//
//        val fragment: Fragment = NotificationsFragment()
//
//        fragmentTransaction.add(R.id.fragmentContainerNotif, fragment)
//    }
//

    private fun validateInputs(): Boolean {
        return when {
            TextUtils.isEmpty(inputMedName?.text.toString().trim { it <= ' ' }) -> {
                showErrorToast(resources.getString(R.string.err_msg_enter_med_name))
                false
            }

            TextUtils.isEmpty(inputDosage?.text.toString().trim { it <= ' ' }) -> {
                showErrorToast(resources.getString(R.string.err_msg_enter_dosage))
                false
            }

            else -> {
                setUpNotifButton.isEnabled = true
                true
            }
        }
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
}