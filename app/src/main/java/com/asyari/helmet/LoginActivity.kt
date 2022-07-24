package com.asyari.helmet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.asyari.helmet.db.DatabaseHelper
import com.asyari.helmet.ui.MainActivity

class LoginActivity: AppCompatActivity() {
    override fun onCreate( savedInstanceState: Bundle? ) {
        getSupportActionBar()?.hide()
        super.onCreate( savedInstanceState )
        setContentView( R.layout.activity_login )

        // Instance
        val txtEmail:    EditText = findViewById( R.id.txtEdit_email )
        val txtPassword: EditText = findViewById( R.id.txtEdit_password )
        val btnLogin:    Button   = findViewById( R.id.btn_login )

        // Event
        btnLogin.setOnClickListener {
            val dbHelper = DatabaseHelper( this )

            // Declare Data
            val email    = txtEmail.text.toString().trim()
            val password = txtPassword.text.toString().trim()

            // Login Check
            val result: Boolean = dbHelper.checkLogin( email, password )

            // Show Message
            if( result == true ) {
                Toast.makeText( this@LoginActivity, "Login berhasil.", Toast.LENGTH_SHORT ).show()
                val intentLogin = Intent( this@LoginActivity, MainActivity::class.java )
                startActivity( intentLogin )
            }
            else {
                Toast.makeText( this@LoginActivity, "Login gagal! Silakan coba lagi.", Toast.LENGTH_SHORT ).show()
            }
        }
    }

    // Function: Register and Forgot Password
    fun register( view: View) {
        val intentRegister = android.content.Intent( this, RegisterActivity::class.java )
        startActivity( intentRegister )
    }
    fun forgot( view: View ) {
        val intentForgot = android.content.Intent( this, RecoveryActivity::class.java )
        startActivity( intentForgot )
    }
}