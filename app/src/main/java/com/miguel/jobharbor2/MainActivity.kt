package com.miguel.jobharbor2

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout

import androidx.navigation.findNavController

import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

import com.miguel.jobharbor2.databinding.ActivityMainBinding

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat


import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.miguel.jobharbor2.data.entity.Usuario
import com.miguel.jobharbor2.viewModel.UsuarioViewModel

class MainActivity : AppCompatActivity() {
    //lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val database =
        Firebase.database("https://jobharbor2-default-rtdb.europe-west1.firebasedatabase.app")
    private val notaRef = database.getReference("notas")
    lateinit var usuarioCargado: Usuario
    val viewModel by viewModels<UsuarioViewModel>()
    enum class ProviderType {
        BASIC
    }

    companion object {
        const val MY_CHANNEL_ID = "myChannel"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host)
        Log.w("Firebase", "Principo")




        createChannel()
        notaRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Este método se llama cada vez que hay una actualización de datos
                if (dataSnapshot.exists()) {

                    createSimpleNotification()

                    Log.d("Firebase", "actualizado")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Maneja errores
                Log.w("Firebase", "loadPost:onCancelled", databaseError.toException())
            }
        })
        Log.w("Firebase", "final")
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.notasFragmento, R.id.usuarioFragmento
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        val emailUsu: TextView = findViewById(R.id.emailUsu);
        val nombreUsu: TextView = findViewById(R.id.nombreUsu);


        val userJsonList = viewModel.getUser(this@MainActivity)

        if (userJsonList != null) {
            for (json in userJsonList) {
                emailUsu.text = json.nombre +" "+ json.ape
                nombreUsu.text = json.email


            }

        }




            return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MY_CHANNEL_ID,
                "Jobharbor",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Canal que habilita las notificaciones"
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createSimpleNotification() {

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val flag =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, flag)

        var builder = NotificationCompat.Builder(this, MY_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_delete)
            .setContentTitle("Nueva Modificación")
            .setContentText("Esto es un ejemplo <3")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Se ha modificado una tarea")
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }
            notify(1, builder.build())
        }
    }

}
