package com.asyari.helmet.ui.menu

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asyari.helmet.R
import com.asyari.helmet.db.DatabaseHelper

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MenuFragment: Fragment() {
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
        val view = inflater.inflate( R.layout.fragment_menu, container, false )

        // Instance
        val rvHelm: RecyclerView = view.findViewById( R.id.rView_menu )
        val dbHelper             = DatabaseHelper( this.requireContext() )

        // Call Function Show Data Menu
        val listData             = dbHelper.showMenu()

        // Set Layout Recycler View
        rvHelm.layoutManager     = LinearLayoutManager( activity )

        // Set Adapter Recycler View
        rvHelm.adapter           = MenuAdapter( listData )

        // Button: Add New Helm
        val buttonAdd: Button = view.findViewById( R.id.btn_addNewHelm )
        buttonAdd.setOnClickListener {
            requireActivity().runOnUiThread {
                startActivity( Intent( context, AddHelmActivity::class.java ) )
            }
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance( param1: String, param2: String ) =
            MenuFragment().apply {
                arguments = Bundle().apply {
                    putString( ARG_PARAM1, param1 )
                    putString( ARG_PARAM2, param2 )
                }
            }
    }
}