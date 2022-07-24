package com.asyari.helmet.ui.menu

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.asyari.helmet.R
import com.asyari.helmet.db.DatabaseHelper
import com.asyari.helmet.ui.MainActivity

class EditHelmActivity : AppCompatActivity() {
    lateinit var image: ImageView
    companion object {
        val IMAGE_REQUEST_CODE = 100

        // Set Variable
        var helmId    = 1
        var helmName  = "Ink T1 Seri 1"
        var helmPrice = 650000
        lateinit var helmImage: Bitmap
    }

    override fun onCreate( savedInstanceState: Bundle? ) {
        getSupportActionBar()?.hide()
        super.onCreate( savedInstanceState )
        setContentView( R.layout.activity_edit_helm )

        // Header
        val goBack: ImageView = findViewById( R.id.img_goBack )
        goBack.setOnClickListener {
            val intentGoBack  = Intent( this, MainActivity::class.java )
            startActivity( intentGoBack )
        }

        // Instance
        val txtId:         TextView = findViewById( R.id.txt_id )
        val txtName:       EditText = findViewById( R.id.txtEdit_updateName )
        val txtPrice:      EditText = findViewById( R.id.txtEdit_updateAddress )
        image                       = findViewById( R.id.img_updateImage )
        val btnAddImage:   Button   = findViewById( R.id.btn_updateImage )
        val btnUpdateHelm: Button   = findViewById( R.id.btn_updateHelm )

        // Data Input
        txtId.text = helmId.toString()
        txtName.setText( helmName )
        txtPrice.setText( helmPrice.toString() )
        image.setImageBitmap( helmImage )

        // Event
        btnAddImage.setOnClickListener {
            pickImageGalery()
        }
        btnUpdateHelm.setOnClickListener {
            val dbHelper = DatabaseHelper( this )

            val id:             Int            = txtId.text.toString().toInt()
            val name:           String         = txtName.text.toString().trim()
            val price:          Int            = txtPrice.text.toString().toInt()
            val bitmapDrawable: BitmapDrawable = image.drawable as BitmapDrawable
            val bitmap:         Bitmap         = bitmapDrawable.bitmap

            val menuModel   = MenuModel( id, name, price, bitmap )
            dbHelper.updateMenu( menuModel )

            val intenUpdate = Intent( this, MainActivity::class.java )
            startActivity( intenUpdate )
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