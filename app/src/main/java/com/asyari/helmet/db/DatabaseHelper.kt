package com.asyari.helmet.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.widget.Toast
import com.asyari.helmet.ui.account.AccountFragment
import com.asyari.helmet.ui.menu.MenuModel
import com.asyari.helmet.ui.transaction.TransactionAdapter
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class DatabaseHelper( var context: Context ): SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {
    companion object {
        // Database
        private val DATABASE_NAME           = "AsyaRI_Helmet"
        private val DATABASE_VERSION        = 1

        // Table and Column
        private val TABLE_ACCOUNT           = "account"
        private val COLUMN_ACCOUNT_EMAIL    = "accountEmail"
        private val COLUMN_ACCOUNT_NAME     = "accountName"
        private val COLUMN_ACCOUNT_ADDRESS  = "accountAddress"
        private val COLUMN_ACCOUNT_PASSWORD = "accountPassword"

        private val TABLE_HELM              = "helm"
        private val COLUMN_HELM_ID          = "helmId"
        private val COLUMN_HELM_NAME        = "helmName"
        private val COLUMN_HELM_PRICE       = "helmPrice"
        private val COLUMN_HELM_IMAGE       = "helmImage"

        private val TABLE_TXN               = "transaksi"
        private val COLUMN_TXN_ID           = "transactionId"
        private val COLUMN_TXN_DATE         = "transactionDate"
        private val COLUMN_TXN_USER         = "transactionUser"

        private val TABLE_TXN_DETS          = "txnDets"
        private val COLUMN_TXN_DETS_ID      = "txnDetsId"
        private val COLUMN_TXN_DETS_TXN_ID  = "transactionId"
        private val COLUMN_TXN_DETS_HELM_ID = "helmId"
        private val COLUMN_TXN_DETS_AMOUNT  = "helmAmount"
        private val COLUMN_TXN_DETS_TOTAL   = "total"
    }

    // SQL Query Create Table
    private val CREATE_ACCOUNT_TABLE = (
            "CREATE TABLE " + TABLE_ACCOUNT   + "("
                    + COLUMN_ACCOUNT_EMAIL    + " TEXT PRIMARY KEY, "
                    + COLUMN_ACCOUNT_NAME     + " TEXT, "
                    + COLUMN_ACCOUNT_ADDRESS  + " TEXT, "
                    + COLUMN_ACCOUNT_PASSWORD + " TEXT)"
            )
    private val CREATE_HELM_TABLE = (
            "CREATE TABLE " + TABLE_HELM      + "("
                    + COLUMN_HELM_ID          + " INT PRIMARY KEY, "
                    + COLUMN_HELM_NAME        + " TEXT, "
                    + COLUMN_HELM_PRICE       + " INT, "
                    + COLUMN_HELM_IMAGE       + " BLOP)"
            )
    private val CREATE_TXN_TABLE = (
            "CREATE TABLE " + TABLE_TXN       + "("
                    + COLUMN_TXN_ID           + " INT PRIMARY KEY, "
                    + COLUMN_TXN_DATE         + " TEXT, "
                    + COLUMN_TXN_USER         + " TEXT)"
            )
    private val CREATE_TXN_DETS_TABLE = (
            "CREATE TABLE " + TABLE_TXN_DETS  + "("
                    + COLUMN_TXN_DETS_ID      + " INT PRIMARY KEY, "
                    + COLUMN_TXN_DETS_TXN_ID  + " INT, "
                    + COLUMN_TXN_DETS_HELM_ID + " INT, "
                    + COLUMN_TXN_DETS_AMOUNT  + " INT, "
                    + COLUMN_TXN_DETS_TOTAL   + " INT)"
            )

    // SQL Query Drop Table
    private val DROP_ACCOUNT_TABLE  = "DROP TABLE IF EXISTS $TABLE_ACCOUNT"
    private val DROP_HELM_TABLE     = "DROP TABLE IF EXISTS $TABLE_HELM"
    private val DROP_TXN_TABLE      = "DROP TABLE IF EXISTS $TABLE_TXN"
    private val DROP_TXN_DETS_TABLE = "DROP TABLE IF EXISTS $TABLE_TXN_DETS"

    // Override Functions
    override fun onCreate( p0: SQLiteDatabase? ) {
        p0?.execSQL( CREATE_ACCOUNT_TABLE )
        p0?.execSQL( CREATE_HELM_TABLE )
        p0?.execSQL( CREATE_TXN_TABLE )
        p0?.execSQL( CREATE_TXN_DETS_TABLE )
    }
    override fun onUpgrade( p0: SQLiteDatabase?, p1: Int, p2: Int ) {
        p0?.execSQL( DROP_ACCOUNT_TABLE )
        p0?.execSQL( DROP_HELM_TABLE )
        p0?.execSQL( DROP_TXN_TABLE )
        p0?.execSQL( DROP_TXN_DETS_TABLE )
        onCreate( p0 )
    }

    // Functions: Create
    fun addAccount( accountEmail:String, accountName:String, accountAddress:String, accountPassword:String ) {
        val db     = this.writableDatabase

        // Data Input
        val values = ContentValues()
        values.put( COLUMN_ACCOUNT_EMAIL,    accountEmail )
        values.put( COLUMN_ACCOUNT_NAME,     accountName )
        values.put( COLUMN_ACCOUNT_ADDRESS,  accountAddress )
        values.put( COLUMN_ACCOUNT_PASSWORD, accountPassword )

        val result = db.insert( TABLE_ACCOUNT, null, values )

        // Show Message
        if( result == ( 0 ).toLong() ) {
            Toast.makeText( context, "Register gagal!", Toast.LENGTH_SHORT ).show()
        }
        else {
            Toast.makeText( context, "Register berhasil. " + "Silakan masuk menggunakan akun baru Anda.", Toast.LENGTH_SHORT ).show()
        }
        db.close()
    }
    fun addMenu( menu: MenuModel) {
        val db     = this.writableDatabase

        // Data Input
        val values = ContentValues()
        values.put( COLUMN_HELM_ID,    menu.id )
        values.put( COLUMN_HELM_NAME,  menu.name )
        values.put( COLUMN_HELM_PRICE, menu.price )

        // Image Input
        val byteOutputStream = ByteArrayOutputStream()
        val imageInByte: ByteArray
        menu.image.compress( Bitmap.CompressFormat.JPEG, 100, byteOutputStream )
        imageInByte = byteOutputStream.toByteArray()
        values.put( COLUMN_HELM_IMAGE, imageInByte )

        val result = db.insert( TABLE_HELM, null, values )

        // Show Message
        if( result == ( 0 ).toLong() ) {
            Toast.makeText( context, "Menu gagal ditambahkan!", Toast.LENGTH_SHORT ).show()
        }
        else {
            Toast.makeText( context, "Menu berhasil ditambahkan.", Toast.LENGTH_SHORT ).show()
        }
        db.close()
    }
    @SuppressLint("NewApi")
    fun addTransaction() {
        val dbInsert = this.writableDatabase
        val dbSelect = this.readableDatabase

        // Variable Declaration
        var lastIdTrans  = 0
        var lastIdDetail = 0
        var newIdTrans   = 0
        var newIdDetail  = 0
        val values = ContentValues()

        // Get Last transactionId
        val cursorTrans:  Cursor = dbSelect.rawQuery(
            "SELECT * FROM $TABLE_TXN", null
        )
        val cursorDetail: Cursor = dbSelect.rawQuery(
            "SELECT * FROM $TABLE_TXN_DETS", null
        )

        if( cursorTrans.moveToLast() ) {
            lastIdTrans  = cursorTrans.getInt( 0 )
        }
        if( cursorDetail.moveToLast() ) {
            lastIdDetail = cursorDetail.getInt( 0 )
        }

        // Set Data
        newIdTrans   = lastIdTrans + 1
        val sdf      = SimpleDateFormat( "yyyy-MM-dd" )
        val tanggal  = sdf.format( Date() )
        val username = AccountFragment.email

        // Insert Data
        values.put( COLUMN_TXN_ID,   newIdTrans )
        values.put( COLUMN_TXN_DATE, tanggal )
        values.put( COLUMN_TXN_USER, username )
        val result = dbInsert.insert( TABLE_TXN, null, values )

        // Show Message
        if( result == ( 0 ).toLong() ) {
            Toast.makeText( context, "Transaksi gagal ditambahkan!", Toast.LENGTH_SHORT ).show()
        }
        else {
            Toast.makeText( context, "Transaksi berhasil ditambahkan.", Toast.LENGTH_SHORT ).show()
        }

        newIdDetail = lastIdDetail + 1
        var i       = 0
        val values2 = ContentValues()
        while( i < TransactionAdapter.listId.count() ) {
            values2.put( COLUMN_TXN_DETS_TXN_ID,  newIdDetail )
            values2.put( COLUMN_TXN_DETS_TXN_ID,  newIdTrans )
            values2.put( COLUMN_TXN_DETS_HELM_ID, TransactionAdapter.listId[i] )
            values2.put( COLUMN_TXN_DETS_AMOUNT,  TransactionAdapter.listAmount[i] )
            val result2 = dbInsert.insert( TABLE_TXN_DETS, null, values2 )

            // Show Message
            if( result2 == ( 0 ).toLong() ) {
                Toast.makeText( context, "Detail transaksi gagal ditambahkan!", Toast.LENGTH_SHORT ).show()
            }
            else {
                Toast.makeText( context, "Detail transaksi verhasil ditambahkan.", Toast.LENGTH_SHORT ).show()
            }

            newIdDetail += 1
            i += 1
        }
        dbSelect.close()
        dbInsert.close()
    }

    // Functions: Read
    @SuppressLint( "Range" )
    fun showMenu(): ArrayList<MenuModel> {
        val db       = this.readableDatabase

        // Storing Data
        val listMenu = ArrayList<MenuModel>()
        var cursor: Cursor?=null

        // Execute Query
        try {
            cursor = db.rawQuery( "SELECT * FROM " + TABLE_HELM, null )
        }
        catch( se: SQLiteException) {
            db.execSQL( CREATE_HELM_TABLE )
            return ArrayList()
        }

        var id:         Int
        var name:       String
        var price:      Int
        var imageArray: ByteArray
        var imageBmp: Bitmap

        if( cursor.moveToFirst() ) {
            do {
                // Get Text Data
                id    = cursor.getInt( cursor.getColumnIndex( COLUMN_HELM_ID ) )
                name  = cursor.getString(  cursor.getColumnIndex( COLUMN_HELM_NAME ) )
                price = cursor.getInt( cursor.getColumnIndex( COLUMN_HELM_PRICE ) )

                // Get Image Data
                imageArray = cursor.getBlob( cursor.getColumnIndex( COLUMN_HELM_IMAGE ) )

                // Convert ByteArray to Bitmap
                val byteInputStram = ByteArrayInputStream( imageArray )
                imageBmp           = BitmapFactory.decodeStream( byteInputStram )
                val model          = MenuModel( id = id, name = name, price = price, image = imageBmp )
                listMenu.add( model )
            }
            while( cursor.moveToNext() )
        }
        return  listMenu
    }

    // Functions: Update
    fun updateAccount( accountEmail:String, accountName:String, accountAddress:String, accountPassword:String ) {
        val db     = this.writableDatabase

        // Data Input
        val values = ContentValues()
        values.put( COLUMN_ACCOUNT_EMAIL,    accountEmail )
        values.put( COLUMN_ACCOUNT_NAME,     accountName )
        values.put( COLUMN_ACCOUNT_ADDRESS,    accountAddress )
        values.put( COLUMN_ACCOUNT_PASSWORD, accountPassword )

        val result = db.update( TABLE_ACCOUNT, values, COLUMN_ACCOUNT_EMAIL + " = ? ", arrayOf( accountPassword ) ).toLong()

        // Show Message
        if( result == ( 0 ).toLong() ) {
            Toast.makeText( context, "Akun gagal diperbarui!", Toast.LENGTH_SHORT ).show()
        }
        else {
            Toast.makeText( context, "Akun berhasil diperbarui.", Toast.LENGTH_SHORT ).show()
        }
        db.close()
    }
    fun updateMenu( menu: MenuModel) {
        val db     = this.writableDatabase

        // Input Data
        val values = ContentValues()
        values.put( COLUMN_HELM_ID,    menu.id )
        values.put( COLUMN_HELM_NAME,  menu.name )
        values.put( COLUMN_HELM_PRICE, menu.price )

        // Input Gambar
        val byteOutputStream = ByteArrayOutputStream()
        val imageInByte: ByteArray
        menu.image.compress( Bitmap.CompressFormat.JPEG, 100, byteOutputStream )
        imageInByte = byteOutputStream.toByteArray()
        values.put( COLUMN_HELM_IMAGE,      imageInByte )

        val result = db.update( TABLE_HELM, values, COLUMN_HELM_ID + " = ? ", arrayOf( menu.id.toString() ) ).toLong()

        // Menampilkan Informasi
        if( result == (0).toLong() ) {
            Toast.makeText( context, "Menu gagal diperbarui!", Toast.LENGTH_SHORT ).show()
        }
        else {
            Toast.makeText( context, "Menu berhasil diperbarui.", Toast.LENGTH_SHORT ).show()
        }
        db.close()
    }

    // Functions: Delete
    fun deleteMenu( id:Int ) {
        val db     = this.writableDatabase

        val result = db.delete( TABLE_HELM, COLUMN_HELM_ID + " = ? ", arrayOf( id.toString() ) ).toLong()

        // Show Message
        if( result == ( 0 ).toLong() ) {
            Toast.makeText( context, "Menu gagal dihapus!", Toast.LENGTH_SHORT ).show()
        }
        else {
            Toast.makeText( context, "Menu berhasil dihapus.", Toast.LENGTH_SHORT ).show()
        }
        db.close()
    }

    // Functions: Check
    @SuppressLint("Range")
    fun checkData( accountEmail:String ): String {
        val db            = this.readableDatabase

        val colums        = arrayOf( COLUMN_ACCOUNT_NAME )
        val selection     = "$COLUMN_ACCOUNT_EMAIL = ?"
        val selectionArgs = arrayOf( accountEmail )
        var name: String  = ""

        val cursor = db.query(
            TABLE_ACCOUNT,  // Table to Query
            colums,         // Colums to Return
            selection,      // Colums for Where Clause
            selectionArgs,  // The Values for Where Clause
            null,   // Group by Rows
            null,    // Filter by Row Groups
            null    // The Sort Order
        )

        if( cursor.moveToFirst() ) {
            name = cursor.getString( cursor.getColumnIndex( COLUMN_ACCOUNT_NAME ) )
        }
        cursor.close()
        return name
    }
    @SuppressLint("Range")
    fun checkLogin(accountEmail:String, accountPassword:String): Boolean {
        val db            = this.readableDatabase

        val colums        = arrayOf( COLUMN_ACCOUNT_EMAIL, COLUMN_ACCOUNT_NAME, COLUMN_ACCOUNT_ADDRESS, COLUMN_ACCOUNT_PASSWORD )
        val selection     = "$COLUMN_ACCOUNT_EMAIL = ? AND $COLUMN_ACCOUNT_PASSWORD = ?"
        val selectionArgs = arrayOf( accountEmail, accountPassword )

        val cursor        = db.query(
            TABLE_ACCOUNT,  // Table to Query
            colums,         // Colums to Return
            selection,      // Colums for Where Clause
            selectionArgs,  // The Values for Where Clause
            null,   // Group by Rows
            null,    // Filter by Row Groups
            null    // The Sort Order
        )

        val cursorCount = cursor.count

        // Check Data Available or Not
        val result: Boolean
        if( cursorCount > 0 ) {
            result = true
            // Set Data
            if( cursor.moveToFirst() ) {
                AccountFragment.email   = cursor.getString( cursor.getColumnIndex( COLUMN_ACCOUNT_EMAIL ) )
                AccountFragment.name    = cursor.getString( cursor.getColumnIndex( COLUMN_ACCOUNT_NAME ) )
                AccountFragment.address = cursor.getString( cursor.getColumnIndex( COLUMN_ACCOUNT_ADDRESS ) )
            }
        }
        else {
            return false
        }
        cursor.close()
        db.close()
        return result
    }
}