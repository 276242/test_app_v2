package com.example.myapplicationmad

import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    fun showErrorSnackBar(message: String, success: Boolean) {
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)

        if (success) {
            snackBar.setBackgroundTint(ContextCompat.getColor(this@BaseActivity, R.color.snackBarSuccessful))
        } else {
            snackBar.setBackgroundTint(ContextCompat.getColor(this@BaseActivity, R.color.snackBarError))
        }

        snackBar.show()
    }

    fun showErrorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun showSuccessToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}