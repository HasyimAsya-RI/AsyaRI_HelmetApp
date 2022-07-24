package com.asyari.helmet.ui.transaction

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asyari.helmet.R
import com.asyari.helmet.db.DatabaseHelper

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TransactionFragment: Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate( savedInstanceState: Bundle? ) {
        super.onCreate( savedInstanceState )
        arguments?.let {
            param1 = it.getString( ARG_PARAM1 )
            param2 = it.getString( ARG_PARAM2 )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate The Layout for This Fragment
        val view = inflater.inflate( R.layout.fragment_transaction, container, false )

        // Instance
        rvTransaction = view.findViewById( R.id.rView_transaction )
        txtOrder      = view.findViewById( R.id.txt_order )
        txtDiscount   = view.findViewById( R.id.txt_discount )
        txtTotal      = view.findViewById( R.id.txt_total )
        btnPayment    = view.findViewById( R.id.btn_payment )

        // Call Display Data
        displayData()

        // Button: Payment Continue
        btnPayment.setOnClickListener {
            val dbHelper = DatabaseHelper( this.requireContext() )

            dbHelper.addTransaction()
            activity?.let {
                val intentPayment = Intent( it, PaymentActivity::class.java )
                it.startActivity( intentPayment )
            }
        }
        return view
    }

    fun displayData() {
        rvTransaction.layoutManager = LinearLayoutManager( activity )
        rvTransaction.adapter       = TransactionAdapter()

        txtOrder.text    = TransactionAdapter.price.toString()
        txtDiscount.text = ( TransactionAdapter.price * TransactionAdapter.discount ).toString()
        txtTotal.text    = ( TransactionAdapter.price - ( TransactionAdapter.price * TransactionAdapter.discount ) ).toString()
    }

    companion object {
        lateinit var rvTransaction: RecyclerView
        lateinit var txtOrder:     TextView
        lateinit var txtDiscount:  TextView
        lateinit var txtTotal:     TextView
        lateinit var btnPayment:   Button

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TransactionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}