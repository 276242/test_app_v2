package com.example.myapplicationmad

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myapplicationmad.firestore.FireStoreClass
import com.example.myapplicationmad.firestore.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : BaseActivity() {

    private var inputNameR: EditText? = null
    private var inputEmailR: EditText? = null
    private var inputPasswordR: EditText? = null
    private var inputPasswordRepeat: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val loginTextView = findViewById<TextView>(R.id.logInTextView)
        loginTextView.setOnClickListener { goToLogin() }


        inputNameR = findViewById(R.id.inputNameR)
        inputEmailR = findViewById(R.id.inputEmailR)
        inputPasswordR = findViewById(R.id.inputPasswordR)
        inputPasswordRepeat = findViewById(R.id.inputPasswordRepeat)


        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton?.setOnClickListener {
            if (validateRegisterDetails()) {
                registerUser()
            }
        }
    }

    fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateRegisterDetails(): Boolean {
        val name = inputNameR?.text?.toString()?.trim { it <= ' ' }
        val email = inputEmailR?.text?.toString()?.trim { it <= ' ' }
        val password = inputPasswordR?.text?.toString()?.trim { it <= ' ' }
        val repPassword = inputPasswordRepeat?.text?.toString()?.trim { it <= ' ' }

        if (name.isNullOrEmpty()) {
            showErrorSnackBar(resources.getString(R.string.err_msg_enter_name), false)
            return false
        }

        if (email.isNullOrEmpty()) {
            showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), false)
            return false
        }

        if (password.isNullOrEmpty()) {
            showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), false)
            return false
        }

        if (password.length < 8) {
            showErrorSnackBar(resources.getString(R.string.err_msg_password_too_short), false)
            return false
        }

        if (!password.any { it.isUpperCase() }) {
            showErrorSnackBar(resources.getString(R.string.err_msg_no_uppercase), false)
            return false
        }

        if (!password.any { it.isDigit() }) {
            showErrorSnackBar(resources.getString(R.string.err_msg_no_digit), false)
            return false
        }

        if (repPassword.isNullOrEmpty()) {
            showErrorSnackBar(resources.getString(R.string.err_msg_enter_rep_password), false)
            return false
        }

        if (password != repPassword) {
            showErrorSnackBar(resources.getString(R.string.err_msg_password_mismatch), false)
            return false
        }

        return true
    }


    private fun registerUser() {
        if (validateRegisterDetails()) {
            val name: String = inputNameR?.text.toString().trim { it <= ' ' }
            val login: String = inputEmailR?.text.toString().trim { it <= ' ' }
            val password: String = inputPasswordR?.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(login, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        showErrorSnackBar(
                            "You have successfully registered. Your user id is ${firebaseUser.uid}",
                            true
                        )

                        val user = User(
                            firebaseUser.uid,
                            name,
                        )
                        FireStoreClass().registerUserFS(this@RegisterActivity, user)

//                        FirebaseAuth.getInstance().signOut()
//                        finish()
                        userRegistrationSuccess()

                    } else {
                        showErrorSnackBar(task.exception!!.message.toString(), false)
                    }
                }
        }
    }


    fun userRegistrationSuccess(){
        Toast.makeText(this@RegisterActivity, resources.getString(R.string.register_success),
            Toast.LENGTH_LONG).show()
    }

}