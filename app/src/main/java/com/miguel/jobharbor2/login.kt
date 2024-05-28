package com.miguel.jobharbor2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.auth.FirebaseAuth


class login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.i("MyActivity", "login2")
        setContentView(R.layout.login)

        setUp()
    }


    private fun setUp() {
        val signUpButton: Button = findViewById(R.id.LogOutButton);
        val emailEditText: TextView = findViewById(R.id.emailEditText);
        val PasswordEditText: TextView = findViewById(R.id.PasswordEditText);
        title = "Autenticación"
        //val signUpButton = findViewById<Button>(R.id.signUpButton)

        //Crea un nuevo usuario

        Log.i("MyActivity", "registrarse")
        Log.i("MyActivity", emailEditText.text.toString())
        Log.i("MyActivity", PasswordEditText.text.toString())
        signUpButton.setOnClickListener {
            Log.i("MyActivity", "registrarse")
            if (emailEditText.text.isNotEmpty() && PasswordEditText.text.isNotEmpty()) {
                //Firebase crea ese usuario en la base de datos
                Log.i("MyActivity", "EntroNuevo")
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    emailEditText.text.toString(), PasswordEditText.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        //si es correcto, cambia a home
                        showHome(it.result?.user?.email ?: "", MainActivity.ProviderType.BASIC)
                    } else {
                        //sino, muestra una Alerta
                        Log.i("MyActivity", "ERROR${it.exception}")
                        showAlert()
                    }
                }
            }
        }
        val loginButton: Button = findViewById(R.id.loginButton);
        //Accede a home, introduciendo los datos
        loginButton.setOnClickListener {
            Log.i("MyActivity", "acceder")
            if (emailEditText.text.isNotEmpty() && PasswordEditText.text.isNotEmpty()) {
                //Comprueba usuario y contraseña
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    emailEditText.text.toString(), PasswordEditText.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        //si es correcto accede a home
                        showHome(it.result?.user?.email ?: "", MainActivity.ProviderType.BASIC)
                    } else {
                        //sino alaerta
                        Log.i("MyActivity", "ERROR${it.exception}")
                        showAlert()
                    }
                }
            }
        }

    }



    private fun showAlert() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("ERROR")
        builder.setMessage("Se ha producido un error autenticado al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String, provider: MainActivity.ProviderType) {

        val homeIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }


        startActivity(homeIntent)



    }
}
