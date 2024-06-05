package com.miguel.jobharbor2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.miguel.jobharbor2.data.entity.Usuario
import com.miguel.jobharbor2.viewModel.UsuarioViewModel

import kotlinx.coroutines.launch


class login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.i("MyActivity", "login2")
        setContentView(R.layout.login)

        setUp()
    }

    val viewModel by viewModels<UsuarioViewModel>()
    private fun setUp() {

        val signUpButton: Button = findViewById(R.id.LogOutButton);
        val emailEditText: TextView = findViewById(R.id.emailEditText);
        val PasswordEditText: TextView = findViewById(R.id.PasswordEditText);


        emailEditText.setText("miguel@miguel.es")
        PasswordEditText.setText("00000000")


        title = "Autenticación"


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

                        lifecycleScope.launch {
                            val id = viewModel.generarCadenaAleatoria().toInt()
                            val nombre = ""
                            val ape = ""
                            val email = emailEditText.text.toString()
                            val contra = PasswordEditText.text.toString()
                            val nuevoUsu = Usuario(id, nombre, ape, contra, email)
                            Log.i("usu", "Nuevo usuario: $nuevoUsu")



                            viewModel.guardaUsu(this@login, listOf(nuevoUsu))
                            viewModel.insert(nuevoUsu)

                        }



                        showHome(it.result?.user?.email ?: "", MainActivity.ProviderType.BASIC)
                    } else {
                        //sino, muestra una Alerta
                        Log.i("MyActivity", "ERROR${it.exception}")
                        showAlert(it.exception)
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

                        Log.i("login", "Login: ${it.result?.user?.email}")

                        lifecycleScope.launch {
                            var email = emailEditText.text.toString()
                            Log.i("Usu", "Login: ${emailEditText.text.toString()}")


                            viewModel.buscarUsu(email).collect() { n ->

                                viewModel.guardaUsu(this@login, n)
                                showHome(
                                    it.result?.user?.email ?: "",
                                    MainActivity.ProviderType.BASIC
                                )
                            }
                        }

                    } else {
                        //sino alaerta
                        Log.i("MyActivity", "ERROR${it.exception}")
                        showAlert(it.exception)
                    }
                }
            }
        }

    }


    private fun showAlert(exception: Exception?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error de Autenticación")

        val mensaje = when (exception) {
            is FirebaseAuthInvalidUserException -> "El usuario no existe."
            is FirebaseAuthInvalidCredentialsException -> "Credenciales inválidas."
            is FirebaseAuthUserCollisionException -> "El usuario ya existe."
            is FirebaseAuthWeakPasswordException -> "Contraseña débil."
            else -> "Fallo en la autenticación: ${exception?.message}"
        }

        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String, provider: MainActivity.ProviderType) {


        val homeIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }

//**Aqui hacer busqueda en la BD**/
        startActivity(homeIntent)


    }
}
