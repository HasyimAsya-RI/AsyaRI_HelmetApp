package com.asyari.helmet.ui.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.asyari.helmet.LoginActivity
import com.asyari.helmet.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AccountFragment: Fragment() {
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
        val view = inflater.inflate( R.layout.fragment_account, container, false )

        // Show Account
        val txtName:    TextView = view.findViewById( R.id.txt_accountName )
        val txtAddress: TextView = view.findViewById( R.id.txt_accountAddress )
        val txtEmail:   TextView = view.findViewById( R.id.txt_accountEmail )

        txtName.text    = name
        txtAddress.text = address
        txtEmail.text   = email

        // Exit
        val rLytExit: RelativeLayout = view.findViewById( R.id.rLyt_exit )
        rLytExit.setOnClickListener {
            startActivity( Intent( context, LoginActivity::class.java ) )
        }

        return view
    }

    companion object {
        var name    = "Muhammad Hasyim Asy'ari"
        var address = "Kabupaten Situbondo"
        var email   = "hasyim@students.amikom.ac.id"

        @JvmStatic
        fun newInstance( param1: String, param2: String ) =
            AccountFragment().apply {
                arguments = Bundle().apply {
                    putString( ARG_PARAM1, param1 )
                    putString( ARG_PARAM2, param2 )
                }
            }
    }
}