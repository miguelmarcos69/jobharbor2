package com.miguel.jobharbor2.viewModel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.common.reflect.TypeToken
import com.miguel.jobharbor2.data.bd.ClienteFirebase
import com.miguel.jobharbor2.data.entity.Usuario
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow

class UsuarioViewModel : ViewModel() {

    val BD = ClienteFirebase()
    var usuario: Usuario? = null
    val usuarioChannel = Channel<UsuarioEvent>()
    val usuarioEvent = usuarioChannel.receiveAsFlow()

    suspend fun insert(usu: Usuario) {
        BD.insertUsuario(usu)

    }

    fun update(usu: Usuario) {
        Log.i("nuevoUSU", usu.ID.toString())

        BD.updateUsu(usu)

    }

    fun delete() {

    }


    fun buscarUsu(email: String): Flow<List<Usuario>> = BD.fetchUsu(email)

    fun guardaUsu(context: Context, user: List<Usuario>) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(user)
        editor.putString("user_key", json)
        editor.apply()

    }

    fun getUser(context: Context): List<Usuario>? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("user_key", null)
        val type = object : TypeToken<List<Usuario>>() {}.type
        return gson.fromJson(json, type)
    }

    sealed class UsuarioEvent {
        data class ShowUndoSnackBar(val msg: String, val usuar: Usuario) : UsuarioEvent()
        object NavigateToNotesFragment : UsuarioEvent()
    }   fun generarCadenaAleatoria(longitud: Int = 8): String {
        val caracteresPermitidos = "0123456789"
        return (1..longitud)
            .map { caracteresPermitidos.random() }
            .joinToString("")
    }



}