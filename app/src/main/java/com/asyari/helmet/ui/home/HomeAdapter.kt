package com.asyari.helmet.ui.home

import android.graphics.Bitmap
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.asyari.helmet.R
import com.asyari.helmet.ui.menu.MenuModel
import com.asyari.helmet.ui.transaction.TransactionAdapter

class HomeAdapter( private val data: ArrayList<MenuModel> ):
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): HomeViewHolder {
        val layoutInflater = LayoutInflater.from( parent?.context )
        val cellForRow     = layoutInflater.inflate( R.layout.cardview_home, parent, false )

        return HomeViewHolder( cellForRow )
    }

    override fun onBindViewHolder( holder: HomeViewHolder, position: Int ) {
        holder.bind( data[position] )
    }

    inner class HomeViewHolder( v:View ):RecyclerView.ViewHolder( v ) {
        val txtId:    TextView
        val imgHelm:  ImageView
        val txtPrice: TextView
        val txtName:  TextView
        val btnBuy:   Button

        init {
            txtId    = v.findViewById( R.id.txt_helmId )
            imgHelm  = v.findViewById( R.id.img_helmImage )
            txtPrice = v.findViewById( R.id.txt_helmPrice )
            txtName  = v.findViewById( R.id.txt_helmName )
            btnBuy   = v.findViewById( R.id.btn_buy )

            btnBuy.setOnClickListener {
                TransactionAdapter.amount = TransactionAdapter.listId.count()

                val jumlahData = TransactionAdapter.amount
                if( jumlahData == 0 ) {
                    TransactionAdapter.listId     += txtId.text.toString().toInt()
                    TransactionAdapter.listImage  += imgHelm.drawable.toBitmap( 80,80,null )
                    TransactionAdapter.listPrice  += txtPrice.text.toString().toInt()
                    TransactionAdapter.listName   += txtName.text.toString()

                    TransactionAdapter.price       = TransactionAdapter.price + txtPrice.text.toString().toInt()
                    TransactionAdapter.listAmount += 1
                    Toast.makeText( v.context,"Pesanan pembelian berhasil.", Toast.LENGTH_SHORT ).show()
                }else{
                    //cek data
                    val cek = TransactionAdapter.listId.find { data -> txtId.text.toString().toInt().equals( data ) }

                    if( cek == null ) {
                        TransactionAdapter.listId     += txtId.text.toString().toInt()
                        TransactionAdapter.listImage  += imgHelm.drawable.toBitmap( 80,80,null )
                        TransactionAdapter.listPrice  += txtPrice.text.toString().toInt()
                        TransactionAdapter.listName   += txtName.text.toString()

                        TransactionAdapter.price       = TransactionAdapter.price + txtPrice.text.toString().toInt()
                        TransactionAdapter.listAmount += 1
                        Toast.makeText( v.context,"Pesanan pembelian berhasil.", Toast.LENGTH_SHORT ).show()
                    }
                    else {
                        var indek: Int = TransactionAdapter.listId.indexOf( txtId.text.toString().toInt() )
                        val qty:   Int = TransactionAdapter.listAmount[indek] + 1

                        TransactionAdapter.price       = TransactionAdapter.price + TransactionAdapter.listPrice[indek]
                        TransactionAdapter.listAmount.set( indek, qty )
                        Toast.makeText( v.context,"Pesanan pembelian berhasil.", Toast.LENGTH_SHORT ).show()
                    }
                }
            }
        }

        fun bind( data: MenuModel ) {
            val image: Bitmap = data.image
            val price: Int    = data.price
            val name:  String = data.name

            imgHelm.setImageBitmap( image )
            txtPrice.text = price.toString()
            txtName.text  = name
        }
    }
}