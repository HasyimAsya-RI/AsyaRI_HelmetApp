package com.asyari.helmet.ui.menu

import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.asyari.helmet.R
import com.asyari.helmet.db.DatabaseHelper
import com.asyari.helmet.ui.MainActivity

class MenuAdapter( private val list: ArrayList<MenuModel> ):
    RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): MenuViewHolder {
        val layoutInflater = LayoutInflater.from( parent?.context )
        val cellForRow     = layoutInflater.inflate( R.layout.cardview_menu, parent, false )

        return MenuViewHolder( cellForRow )
    }

    override fun onBindViewHolder( holder: MenuViewHolder, position: Int ) {
        holder.bind( list[position] )
    }

    inner class MenuViewHolder( v: View ): RecyclerView.ViewHolder( v ) {
        val txtId:     TextView
        val imgHelm:   ImageView
        val txtPrice:  TextView
        val txtName:   TextView
        val btnDelete: Button
        val btnUpdate: RelativeLayout

        val context = v.context

        init {
            txtId     = v.findViewById( R.id.txt_helmId )
            imgHelm   = v.findViewById( R.id.img_helmImage )
            txtPrice  = v.findViewById( R.id.txt_helmPrice )
            txtName   = v.findViewById( R.id.txt_helmName )
            btnDelete = v.findViewById( R.id.btn_helmDelete )
            btnUpdate = v.findViewById( R.id.rLyt_menu )

            btnDelete.setOnClickListener {
                val dbHelper     = DatabaseHelper( context )
                dbHelper.deleteMenu( txtId.text.toString().toInt() )

                val intentDelete = Intent( context, MainActivity::class.java )
                context.startActivity( intentDelete )
            }

            btnUpdate.setOnClickListener {
                EditHelmActivity.helmId    = txtId.text.toString().toInt()
                EditHelmActivity.helmImage = imgHelm.drawable.toBitmap( 150, 150, null )
                EditHelmActivity.helmPrice = txtPrice.text.toString().toInt()
                EditHelmActivity.helmName  = txtName.text.toString()

                Toast.makeText( context, EditHelmActivity.helmId.toString(), Toast.LENGTH_SHORT ).show()

                val intentUpdate = Intent( context, EditHelmActivity::class.java )
                context.startActivity( intentUpdate )
            }
        }

        fun bind( data: MenuModel) {
            val id:    Int    = data.id
            val image: Bitmap = data.image
            val price: Int    = data.price
            val name:  String = data.name

            txtId.text    = id.toString()
            imgHelm.setImageBitmap( image )
            txtPrice.text = price.toString()
            txtName.text  = name
        }
    }
}