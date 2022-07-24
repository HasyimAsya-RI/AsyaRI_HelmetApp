package com.asyari.helmet.ui.menu

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.asyari.helmet.R
import com.asyari.helmet.db.DatabaseHelper
import com.asyari.helmet.ui.MainActivity

class AddHelmActivity : AppCompatActivity() {
    lateinit var image: ImageView
    companion object {
        val IMAGE_REQUEST_CODE = 100
    }

    override fun onCreate( savedInstanceState: Bundle? ) {
        getSupportActionBar()?.hide()
        super.onCreate( savedInstanceState )
        setContentView( R.layout.activity_add_helm )

        // Header
        val goBack: ImageView = findViewById( R.id.img_goBack )
        goBack.setOnClickListener {
            val intentGoBack  = Intent( this, MainActivity::class.java )
            startActivity( intentGoBack )
        }

        // Instance
        val txtId:       EditText = findViewById( R.id.txtEdit_addId )
        val txtName:     EditText = findViewById( R.id.txtEdit_addName )
        val txtPrice:    EditText = findViewById( R.id.txtEdit_addAddress )
        image                     = findViewById( R.id.img_addImage )
        val btnAddImage: Button   = findViewById( R.id.btn_addImage )
        val btnSaveMenu: Button   = findViewById( R.id.btn_addHelm )

        // Event
        btnAddImage.setOnClickListener {
            pickImageGalery()
        }
        btnSaveMenu.setOnClickListener {
            val dbHelper = DatabaseHelper( this )

            val id:             Int            = txtId.text.toString().toInt()
            val name:           String         = txtName.text.toString().trim()
            val price:          Int            = txtPrice.text.toString().toInt()
            val bitmapDrawable: BitmapDrawable = image.drawable as BitmapDrawable
            val bitmap:         Bitmap         = bitmapDrawable.bitmap

            val menuModel  = MenuModel( id, name, price, bitmap )
            dbHelper.addMenu( menuModel )

            val intentSave = Intent( this, MainActivity::class.java )
            startActivity( intentSave )
        }
    }

    private fun pickImageGalery() {
        val intentPickImage  = Intent( Intent.ACTION_PICK )
        intentPickImage.type = "image/"
        startActivityForResult( intentPickImage, IMAGE_REQUEST_CODE )
    }

    override fun onActivityResult( requestCode:Int, resultCode:Int, data: Intent? ) {
        super.onActivityResult( requestCode, resultCode, data )
        if( requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK ) {
            image.setImageURI( data?.data )
        }
    }
}