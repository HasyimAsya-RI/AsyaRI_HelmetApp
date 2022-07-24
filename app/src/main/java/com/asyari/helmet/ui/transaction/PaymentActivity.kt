package com.asyari.helmet.ui.transaction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.asyari.helmet.R

class PaymentActivity : AppCompatActivity() {
    override fun onCreate( savedInstanceState: Bundle? ) {
        getSupportActionBar()?.hide()
        super.onCreate( savedInstanceState )
        setContentView( R.layout.activity_payment )

        val txtTotal:  TextView = findViewById( R.id.txt_totalPurchase )
        val txtChange: TextView = findViewById( R.id.txt_change )
        val txtCash:   EditText = findViewById( R.id.txtEdit_cash )
        val btnFinish: Button   = findViewById( R.id.btn_finish )

        txtTotal.text  = (
                TransactionAdapter.price
                        - ( TransactionAdapter.price * TransactionAdapter.discount )
                ).toString()
        txtChange.text = "0"

        btnFinish.setOnClickListener {
            val kembali = ( txtCash.text.toString().toDouble() ) - ( txtTotal.text.toString().toDouble() )
            txtChange.setText( kembali.toString() )

            // Clear Data
            TransactionAdapter.listId.clear()
            TransactionAdapter.listImage.clear()
            TransactionAdapter.listPrice.clear()
            TransactionAdapter.listName.clear()
            TransactionAdapter.listAmount.clear()
            TransactionAdapter.price  = 0
            TransactionAdapter.amount = 0
        }
    }
}