package com.miguel.jobharbor2.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miguel.jobharbor2.data.bd.ClienteFirebase
import com.miguel.jobharbor2.data.entity.Notas
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

import kotlinx.coroutines.launch
import javax.inject.Inject


class NotasViewModel : ViewModel() {


    val BD = ClienteFirebase()
    val notas = BD.fetchNotas();
    val notesChannel = Channel<NotesEvent>()
    val notesEvent = notesChannel.receiveAsFlow()

    fun insert(nota: Notas) = viewModelScope.launch {
        Log.i("MyActivity", "NotasViewModel")
        BD.insert(nota)
    }


    fun update(nota: Notas) = viewModelScope.launch {
        BD.update(nota)
    }


    fun delete(nota: Notas) = viewModelScope.launch {
        BD.delete(nota)
    }


    sealed class NotesEvent {
        data class ShowUndoSnackBar(val msg: String, val note: Notas) : NotesEvent()
        object NavigateToNotesFragment : NotesEvent()
    }


}