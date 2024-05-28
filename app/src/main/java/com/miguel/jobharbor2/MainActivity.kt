package com.miguel.jobharbor2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.miguel.jobharbor2.data.bd.ClienteFirebase
import com.miguel.jobharbor2.data.entity.Notas
import com.miguel.jobharbor2.iu.notasFragmento
import com.miguel.jobharbor2.login

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController

    enum class ProviderType {
        BASIC
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        val navHotFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHotFragment.navController

        setupActionBarWithNavController(navController)

    }
    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || navController.navigateUp()
    }
}