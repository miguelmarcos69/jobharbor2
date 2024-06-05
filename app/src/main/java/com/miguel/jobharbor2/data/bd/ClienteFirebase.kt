package com.miguel.jobharbor2.data.bd


import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database


import com.miguel.jobharbor2.data.entity.Notas
import com.miguel.jobharbor2.data.entity.Usuario

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

import kotlinx.coroutines.suspendCancellableCoroutine

import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException



class ClienteFirebase {
    private val database =
        Firebase.database("https://jobharbor2-default-rtdb.europe-west1.firebasedatabase.app")
    private val notaRef = database.getReference("notas")
    private val usuRef = database.getReference("usuarios")


    suspend fun insert(Nnota: Notas): Boolean {
        val key = notaRef.push().key
        Log.i("MyActivity", "lectura2")
        Log.i("MyActivity", Nnota.toString())

        return suspendCancellableCoroutine { continuation ->
            key?.let {
                Log.i("MyActivity", "insert  ")
                notaRef.child(Nnota.ID.toString()).setValue(Nnota).addOnSuccessListener {
                    continuation.resume(true)
                }.addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                    Log.i("MyActivity", e.toString())


                }
            }
        }

    }


    fun update(Nnota: Notas) {

        val updatedNota = mapOf<String, Any>(
            "contenido" to Nnota.contenido, "fecha" to Nnota.fecha, "titulo" to Nnota.titulo
        )

        if (Nnota.ID != null) {
            Log.i("MyActivity", Nnota.ID.toString())

            notaRef.child(Nnota.ID.toString()).updateChildren(updatedNota).addOnSuccessListener {
                // Datos actualizados con éxito
                println("Datos actualizados con éxito")
            }.addOnFailureListener { error ->
                // Error al actualizar los datos
                println("Error al actualizar los datos: ${error.message}")

            }
        }
    }

    fun updateUsu(Nusu: Usuario) {


        val updatedNota = mapOf<String, Any>(
            "nombre" to Nusu.nombre,
            "ape" to Nusu.ape,
            "contra" to Nusu.contra,
            "email" to Nusu.email
        )

        if (Nusu.ID != null) {
            Log.i("MyActivity", Nusu.ID.toString())

            usuRef.child(Nusu.ID.toString()).updateChildren(updatedNota).addOnSuccessListener {
                // Datos actualizados con éxito
                println("Datos actualizados con éxito")
            }.addOnFailureListener { error ->
                // Error al actualizar los datos
                println("Error al actualizar los datos: ${error.message}")

            }
        }
    }

    fun delete(Nnota: Notas) {


        if (Nnota.ID != null) {
            Log.i("MyActivity", Nnota.ID.toString())

            notaRef.child(Nnota.ID.toString()).removeValue().addOnSuccessListener {
                // Nota eliminada con éxito
                Log.i("nota", "Nota eliminada con éxito")

            }.addOnFailureListener { error ->
                // Error al eliminar la nota
                Log.i("nota", "Error al eliminar la nota: ${error.message}")
            }
        }
    }


    fun fetchNotas(): Flow<List<Notas>> = callbackFlow {

        Log.i("MyActivity", "lectura1")
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val notas = dataSnapshot.children.mapNotNull {
                    it.getValue(Notas::class.java)
                }
                Log.i("MyActivity", "lectura2")
                trySend(notas).isSuccess // Ofrece los datos al Flow
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Maneja el error aquí
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        notaRef.addValueEventListener(listener)

        awaitClose { notaRef.removeEventListener(listener) } // Elimina el listener cuando el Flow se cierra
    }


    fun fetchUsu(email: String): Flow<List<Usuario>> = callbackFlow {

        Log.i("MyActivity", "lectura1")
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val usuarios = dataSnapshot.children.mapNotNull { snapshot ->
                    val usuario = snapshot.getValue(Usuario::class.java)
                    Log.i("usu", "Usuario obtenido:  $usuario")
                    Log.i("usu", "email $email")
                    if (usuario != null && usuario.email == email) {
                        Log.i("usu", "Email coincide: ${usuario.email}")
                        usuario
                    } else {
                        null
                    }
                }
                Log.i("usu", "Usuarios filtrados: $usuarios")
                trySend(usuarios).isSuccess // Ofrece los datos al Flow
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Maneja el error aquí
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        //usuRef.addValueEventListener(listener)
        usuRef.addListenerForSingleValueEvent(listener)
        awaitClose { usuRef.removeEventListener(listener) }
        // Elimina el listener cuando el Flow se cierra
    }

    suspend fun insertUsuario(Nusu: Usuario): Boolean {
        val key = usuRef.push().key
        Log.i("MyActivity", "lectura2")
        Log.i("MyActivity", Nusu.toString())

        return suspendCancellableCoroutine { continuation ->
            key?.let {
                Log.i("MyActivity", "insert  $Nusu")
                usuRef.child(Nusu.ID.toString()).setValue(Nusu).addOnSuccessListener {

                    continuation.resume(true)
                }.addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                    Log.i("MyActivity", e.toString())


                }
            }
        }
    }
}
