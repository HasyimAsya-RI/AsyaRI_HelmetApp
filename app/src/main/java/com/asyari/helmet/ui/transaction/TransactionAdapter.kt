package com.asyari.helmet.ui.transaction

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.asyari.helmet.R

class TransactionAdapter: RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    companion object{
        var listId            = mutableListOf<Int>()
        var listImage         = mutableListOf<Bitmap>()
        var listPrice         = mutableListOf<Int>()
        var listName          = mutableListOf<String>()
        var listAmount        = mutableListOf<Int>()
        var amount:    Int    = 0
        var price:     Int    = 0
        var discount:  Double = 0.45
    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): TransactionViewHolder {
        val layoutInflater = LayoutInflater.from( parent?.context )
        val cellForRow     = layoutInflater.inflate(
            R.layout.cardview_transaction,
            parent, false )

        return TransactionViewHolder( cellForRow )
    }

    override fun onBindViewHolder( holder: TransactionViewHolder, position: Int ) {
        holder.txtId.text     = listId[position].toString()
        holder.imgHelm.setImageBitmap( listImage[position] )
        holder.txtPrice.text  = listPrice[position].toString()
        holder.txtName.text   = listName[position]
        holder.txtAmount.text = listAmount[position].toString()
    }

    override fun getItemCount(): Int {
        return listId.size
    }

    inner class TransactionViewHolder( v: View ): RecyclerView.ViewHolder( v ) {
        val txtId:     TextView
        val imgHelm:   ImageView
        val txtPrice:  TextView
        val txtName:   TextView
        val txtAmount: TextView
        val imgAdd:    ImageView
        val imgRemove: ImageView
        val btnDelete: Button

        val context = v.context

        init {
            txtId     = v.findViewById( R.id.txt_helmId )
            imgHelm   = v.findViewById( R.id.img_helmImage )
            txtPrice  = v.findViewById( R.id.txt_helmPrice )
            txtName   = v.findViewById( R.id.txt_helmName )
            txtAmount = v.findViewById( R.id.txt_helmAmount )
            imgAdd    = v.findViewById( R.id.img_helmAdd )
            imgRemove = v.findViewById( R.id.img_helmRemove )
            btnDelete = v.findViewById( R.id.btn_helmDelete )

            imgAdd.setOnClickListener {
                val qty:Int = txtAmount.text.toString().toInt()

                txtAmount.text = ( qty + 1 ).toString()
                price += txtPrice.text.toString().toInt()

                TransactionFragment.txtOrder.text    = price.toString()
                TransactionFragment.txtDiscount.text = ( price * discount ).toString()
                TransactionFragment.txtTotal.text    = ( price - ( price * discount ) ).toString()
            }

            imgRemove.setOnClickListener {
                val qty:Int = txtAmount.text.toString().toInt()

                if( qty > 1 ){
                    txtAmount.text = ( qty - 1 ).toString()
                    price      -= txtPrice.text.toString().toInt()

                    TransactionFragment.txtOrder.text    = price.toString()
                    TransactionFragment.txtDiscount.text = ( price * discount ).toString()
                    TransactionFragment.txtTotal.text    = ( price - ( price * discount ) ).toString()
                }
            }
        }

    }
}