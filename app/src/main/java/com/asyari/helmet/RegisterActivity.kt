package com.asyari.helmet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.asyari.helmet.db.DatabaseHelper

class RegisterActivity: AppCompatActivity() {
    override fun onCreate( savedInstanceState: Bundle? ) {
        getSupportActionBar()?.hide()
        super.onCreate( savedInstanceState )
        setContentView( R.layout.activity_register )

        // Instance
        val txtEmail:    EditText = findViewById( R.id.txtEdit_email )
        val txtName:     EditText = findViewById( R.id.txtEdit_name )
        val txtAddress:  EditText = findViewById( R.id.txtEdit_address )
        val txtPassword: EditText = findViewById( R.id.txtEdit_password )
        val btnRegister: Button   = findViewById( R.id.btn_register )

        // Event
        btnRegister.setOnClickListener {
            val dbHelper = DatabaseHelper( this )

            // Declare Data
            val email:    String = txtEmail.text.toString().trim()
            val name:     String = txtName.text.toString().trim()
            val address:  String = txtAddress.text.toString().trim()
            val password: String = txtPassword.text.toString().trim()

            // Email Check
            val data: String = dbHelper.checkData( email )

            // Show Message
            if( data == "" ) {
                dbHelper.addAccount( email, name, address, password )
                val intentRegister = Intent( this@RegisterActivity, LoginActivity::class.java )
                startActivity( intentRegister )
            }
            else {
                Toast.makeText( this@RegisterActivity, "Register gagal! " + "Email Anda sudah terdaftar.", Toast.LENGTH_SHORT ).show()
            }
        }
    }

    fun back_toLogin( imageView: View ) {
        val intentGoBack = android.content.Intent( this, LoginActivity::class.java )
        startActivity( intentGoBack )
    }
}