package com.asyari.helmet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.asyari.helmet.ui.MainActivity

class RecoveryActivity: AppCompatActivity() {
    override fun onCreate( savedInstanceState: Bundle? ) {
        getSupportActionBar()?.hide()
        super.onCreate( savedInstanceState )
        setContentView( R.layout.activity_recovery )

        // Instance
        val btnSearch: Button = findViewById( R.id.btn_search )

        // Event
        btnSearch.setOnClickListener {
            val intentSearch = Intent( this, MainActivity::class.java )
            startActivity( intentSearch )
        }
    }

    fun back_toLogin( imageView: View ) {
        val intentGoBack = android.content.Intent( this, LoginActivity::class.java )
        startActivity( intentGoBack )
    }
}