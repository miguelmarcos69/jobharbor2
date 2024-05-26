package com.miguel.jobharbor2.data.bd


import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database

import com.miguel.jobharbor2.data.entity.Notas
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class ClienteFirebase {
    private val database =
        Firebase.database("https://jobharbor2-default-rtdb.europe-west1.firebasedatabase.app")
    private val notaRef = database.getReference("notas")


    suspend fun insert(Nnota: Notas): Boolean {
        val key = notaRef.push().key
        Log.i("MyActivity", "lectura2")
        Log.i("MyActivity", Nnota.toString())

        return suspendCancellableCoroutine { continuation ->
            key?.let {
                Log.i("MyActivity", "insert  ")
                notaRef.child(Nnota.ID.toString()).setValue(Nnota)
                    .addOnSuccessListener {
                        continuation.resume(true)
                    }
                    .addOnFailureListener { e ->
                        continuation.resumeWithException(e)
                        Log.i("MyActivity", e.toString())


                    }
            }
        }
    }

    fun update(Nnota: Notas) {

        val updatedNota = mapOf<String, Any>(
            "contenido" to Nnota.contenido,
            "fecha" to Nnota.fecha,
            "titulo" to Nnota.titulo
        )

        if (Nnota.ID != null) {
            Log.i("MyActivity", Nnota.ID.toString())

            notaRef.child(Nnota.ID.toString()).updateChildren(updatedNota)
                .addOnSuccessListener {
                    // Datos actualizados con éxito
                    println("Datos actualizados con éxito")
                }
                .addOnFailureListener { error ->
                    // Error al actualizar los datos
                    println("Error al actualizar los datos: ${error.message}")

                }
        }
    }


    fun delete(Nnota: Notas) {


        if (Nnota.ID != null) {
            Log.i("MyActivity", Nnota.ID.toString())

            notaRef.child(Nnota.ID.toString()).removeValue()
                .addOnSuccessListener {
                    // Nota eliminada con éxito
                    println("Nota eliminada con éxito")
                }
                .addOnFailureListener { error ->
                    // Error al eliminar la nota
                    println("Error al eliminar la nota: ${error.message}")
                }
        }
    }


    fun fetchNotas(): Flow<List<Notas>> = callbackFlow {

        Log.i("MyActivity", "lectura1")
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val notas = dataSnapshot.children.mapNotNull {

                    it.key?.let { it1 ->
                        Log.i("MyActivity", it1)
                        Log.i("MyActivity", it.toString())
                    }

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


}
