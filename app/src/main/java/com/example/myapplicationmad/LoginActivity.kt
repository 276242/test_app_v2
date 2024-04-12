package com.example.myapplicationmad

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity() {

    private var inputEmailL: EditText? = null
    private var inputPasswordL: EditText? = null
    private var loginButton: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val registerTextView = findViewById<TextView>(R.id.registerTextView)
        registerTextView.setOnClickListener {goToRegister(it) }

        inputEmailL = findViewById(R.id.inputEmailL)
        inputPasswordL = findViewById(R.id.inputPasswordL)
        loginButton = findViewById(R.id.loginButton)


        loginButton?.setOnClickListener{
            loginUser()
        }

    }


    private fun goToRegister (view : View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }



    private fun validateLoginDetails(): Boolean {
        val email = inputEmailL?.text?.toString()?.trim { it <= ' ' }
        val password = inputPasswordL?.text?.toString()?.trim { it <= ' ' }

        if (email.isNullOrEmpty()) {
            showErrorToast(resources.getString(R.string.err_msg_enter_email))
            return false
        }

        if (password.isNullOrEmpty()) {
            showErrorToast(resources.getString(R.string.err_msg_enter_password))
            return false
        }

        return true
    }


    private fun loginUser() {
        if (validateLoginDetails()) {
            val email = inputEmailL?.text.toString().trim { it <= ' ' }
            val password = inputPasswordL?.text.toString().trim { it <= ' ' }


            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{ task ->

                        if(task.isSuccessful){
                            showErrorToast("Login successful!")
                            goToMainActivity()
                            finish()

                        } else {
                            showErrorSnackBar(task.exception!!.message.toString(),true)
                        }
                    }

        }
    }

    fun goToMainActivity(){

        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("userId", userId)
        startActivity(intent)
        finish()
    }

}