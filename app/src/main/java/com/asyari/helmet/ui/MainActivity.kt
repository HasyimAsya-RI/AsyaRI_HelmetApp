package com.asyari.helmet.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.asyari.helmet.R
import com.asyari.helmet.ui.account.AccountFragment
import com.asyari.helmet.ui.home.HomeFragment
import com.asyari.helmet.ui.menu.MenuFragment
import com.asyari.helmet.ui.transaction.TransactionFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity: AppCompatActivity() {

    override fun onCreate( savedInstanceState: Bundle? ) {
        getSupportActionBar()?.hide()
        super.onCreate( savedInstanceState )
        setContentView( R.layout.activity_main )

        // Instance
        val btmNavigation: BottomNavigationView = findViewById( R.id.btm_navigation )
        val fragHome                            = HomeFragment()
        val fragMenu                            = MenuFragment()
        val fragTransaction                     = TransactionFragment()
        val fragAccount                         = AccountFragment()

        replaceFragment( fragHome )

        // Event
        btmNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_home        -> replaceFragment( fragHome )
                R.id.nav_menu        -> replaceFragment( fragMenu )
                R.id.nav_transaction -> replaceFragment( fragTransaction )
                R.id.nav_account     -> replaceFragment( fragAccount )
            }
            true
        }
    }

    private fun replaceFragment( fragment: Fragment ) {
        supportFragmentManager.beginTransaction().apply {
            replace( R.id.frag_container, fragment )
            commit()
        }
    }
}