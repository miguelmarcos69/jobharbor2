package com.miguel.jobharbor2.iu

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.miguel.jobharbor2.R
import com.miguel.jobharbor2.adaptador.NotasAdaptador
import com.miguel.jobharbor2.data.entity.Notas

import com.miguel.jobharbor2.databinding.FragmentNotasBinding
import com.miguel.jobharbor2.viewModel.NotasViewModel
import kotlinx.coroutines.launch

class notasFragmento : Fragment(R.layout.fragment_notas), NotasAdaptador.OnNoteClickListener {

    val viewModel by viewModels<NotasViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentNotasBinding.bind(requireView())
        Log.i("MyActivity", "entro")
        binding.apply {
//Genera la lista de notas
            recyclerViewNotas.layoutManager = GridLayoutManager(context, 2)
            recyclerViewNotas.setHasFixedSize(true)

            anadirBtn.setOnClickListener {
                val action = notasFragmentoDirections.actionNotasFragmentoToEditarNotas(null)
                findNavController().navigate(action)
            }


            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.notas.collect() { notas ->
                    // `nota` es una instancia de la clase Nota
                    // Puedes acceder a los datos así:
                    Log.i("notas", "Notas recibidas: $notas")
                    val adapter = NotasAdaptador(notas, this@notasFragmento)
                    recyclerViewNotas.adapter = adapter

                    // Ahora puedes usar `titulo`, `contenido` y `fecha` para actualizar tu UI
                }
            }
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.notesEvent.collect { event ->
                    if (event is NotasViewModel.NotesEvent.ShowUndoSnackBar) {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG)
                            .setAction("UNDO") {
                                viewModel.insert(event.note)
                            }.show()
                    }
                }
            }

        }


    }

    override fun onNoteClick(note: Notas) {
        Log.i("notas", "Editar: $note")
        val action = notasFragmentoDirections.actionNotasFragmentoToEditarNotas(note)
        findNavController().navigate(action)
    }

    override fun onNoteLongClick(note: Notas) {
        Log.i("MyActivity", "Borrar")
        viewModel.delete(note)
    }


}