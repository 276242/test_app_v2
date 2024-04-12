package com.example.myapplicationmad.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.example.myapplicationmad.R
import com.example.myapplicationmad.SpanOfOneBoxActivity
import com.example.myapplicationmad.firestore.MedBox
import com.example.myapplicationmad.firestore.Medication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class AddNewBoxFragment : Fragment(){

    private lateinit var backButton: ImageButton
    private lateinit var inputMedName: EditText
    private lateinit var inputNumbDoses: EditText
    private lateinit var inputDaysBefore: EditText
    private lateinit var saveButton: Button
    private lateinit var switchIfNotifs: Switch
    private var onBoxSavedListener: OnBoxSavedListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_add_new_box, container, false)

        backButton = rootView.findViewById<ImageButton>(R.id.backButton)
        inputDaysBefore = rootView.findViewById(R.id.inputDaysBefore)
        saveButton = rootView.findViewById(R.id.saveButton2)
        inputMedName = rootView.findViewById(R.id.inputMedName)
        inputNumbDoses = rootView.findViewById(R.id.inputNumbDoses)
        switchIfNotifs = rootView.findViewById(R.id.switchIfNotifs)
        val textView = rootView.findViewById<TextView>(R.id.textView5)

        switchIfNotifs.setOnCheckedChangeListener { _, isChecked ->
            inputDaysBefore.visibility = if (isChecked) View.VISIBLE else View.GONE
            switchIfNotifs.text = "YES"
            textView.visibility = View.VISIBLE
            inputDaysBefore.visibility = View.VISIBLE
            if (!isChecked) {
                switchIfNotifs.text = "NO"
                textView.visibility = View.GONE
                inputDaysBefore.visibility = View.GONE
            }
        }

        backButton.setOnClickListener {
            val activity: FragmentActivity? = activity
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

        saveButton.setOnClickListener {
            saveNewBox()
            goToSpanOfOneBoxActivity()
        }

        return rootView
    }

    private fun goToSpanOfOneBoxActivity() {
        val activity = activity as SpanOfOneBoxActivity
        activity.goToSpanOfOneBoxActivity()
    }

    private fun saveNewBox() {
        val medName = inputMedName.text.toString().trim()
        val numbDoses = inputNumbDoses.text.toString().toIntOrNull() ?: 0
        val ifNotifs = switchIfNotifs.isChecked
        val daysBefore = if (ifNotifs) inputDaysBefore.text.toString().toIntOrNull() ?: 0 else 0

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        val startDate = System.currentTimeMillis()

        val medication = Medication(medName, "", "")
        val medBox = MedBox(medication
            , ifNotifs, daysBefore, daysTillNotification = if (ifNotifs) numbDoses - daysBefore else 0, startDate, numbDoses)

        val medBoxesCollection = db.collection("medBoxes").document(userId ?: "").collection("userMedBoxes")

        medBoxesCollection.add(medBox)
            .addOnSuccessListener { documentReference ->
                activity?.onBackPressedDispatcher?.onBackPressed()
                onBoxSavedListener?.onBoxSaved()
            }
            .addOnFailureListener { exception ->
            }
    }


//    private fun showMedSetupMessage() {
//        val alertDialogBuilder = AlertDialog.Builder(requireContext())
//        alertDialogBuilder.setMessage("Medication Not Found! Please add this medication first.")
//        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
//            dialog.dismiss()
//            activity?.onBackPressedDispatcher?.onBackPressed()
//        }
//
//        val alertDialog = alertDialogBuilder.create()
//        alertDialog.show()
//    }
//
//
//    private fun checkIfMedicationExists(medName: String, callback: (String?) -> Unit) {
//        val userId = FirebaseAuth.getInstance().currentUser?.uid
//        val db = FirebaseFirestore.getInstance()
//
//        if (userId != null) {
//            db.collection("users")
//                .document(userId)
//                .collection("medications")
//                .whereEqualTo("name", medName)
//                .get()
//                .addOnSuccessListener { documents ->
//                    if (!documents.isEmpty) {
//                        val documentId = documents.documents[0].id
//                        callback.invoke(documentId)
//                    } else {
//                        callback.invoke(null)
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    callback.invoke(null)
//                }
//        }
//    }

//    fun setOnBoxSavedListener(listener: SpanOfOneBoxActivity) {
//        this.onBoxSavedListener = listener
//    }

    interface OnBoxSavedListener {
        fun onBoxSaved()
    }

    companion object {
        fun newInstance(name: String): Fragment {
            TODO("Not yet implemented")
        }
    }
}