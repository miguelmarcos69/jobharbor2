package com.miguel.jobharbor2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.miguel.jobharbor2.data.bd.ClienteFirebase
import com.miguel.jobharbor2.data.entity.Notas
import com.miguel.jobharbor2.databinding.ActivityMainBinding
import com.miguel.jobharbor2.iu.notasFragmento
import com.miguel.jobharbor2.login

class MainActivity : AppCompatActivity() {
    //lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    enum class ProviderType {
        BASIC
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
/*
        val navHotFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
         navController = navHotFragment.navController
*/
        val navController = findNavController(R.id.nav_host)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.notasFragmento
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        /*
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        val navHotFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHotFragment.navController

        setupActionBarWithNavController(navController)*/

    }
/*
    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || navController.navigateUp()
    }*/
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}