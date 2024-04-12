package com.example.myapplicationmad.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.myapplicationmad.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ConfirmMedicationFragment : Fragment() {

    private lateinit var backButton: ImageView
    private lateinit var medicationNameTextView: TextView
    private lateinit var takenCheckBox: CheckBox
    private lateinit var confirmButton: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_confirm_medication, container, false)

        medicationNameTextView = rootView.findViewById(R.id.tvMedicationName)
        takenCheckBox = rootView.findViewById(R.id.checkboxTaken)
        backButton = rootView.findViewById(R.id.backButton) as ImageView
        confirmButton = rootView.findViewById(R.id.confirmButton)


        backButton.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        confirmButton.setOnClickListener {
            val isTaken = takenCheckBox.isChecked
            val medicationName = medicationNameTextView.text.toString()

            //saveMedicationConfirmationToDatabase(medicationName, isTaken)
        }

        return rootView
    }


//    private fun saveMedicationConfirmationToDatabase(medicationName: String, isTaken: Boolean) {
//        val userId = FirebaseAuth.getInstance().currentUser?.uid
//
//        val db = FirebaseFirestore.getInstance()
//        val medicationRef = db.collection("medications").document(medicationName)
//
//        val data = hashMapOf("medicationName" to medicationName, "isTaken" to isTaken)
//
//        medicationRef.set(data)
//            .addOnSuccessListener {
//                activity?.onBackPressedDispatcher?.onBackPressed()
//            }
//            .addOnFailureListener {
//
//            }
//    }

    companion object {
        fun newInstance(name: String): Fragment {
            val fragment = ConfirmMedicationFragment()
            val args = Bundle()
            args.putString("medicationName", name)
            fragment.arguments = args
            return fragment
        }
    }
}
//
//    companion object {
//
//        const val MEDICATION_NAME_KEY = "medicationName"
//
//        fun newInstance(medicationName: String): ConfirmMedicationFragment {
//            val fragment = ConfirmMedicationFragment()
//            val args = Bundle()
//            args.putString(MEDICATION_NAME_KEY, medicationName)
//            fragment.arguments = args
//            return fragment
//        }
//    }

