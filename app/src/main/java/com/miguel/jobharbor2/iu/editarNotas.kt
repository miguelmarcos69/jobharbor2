package com.miguel.jobharbor2.iu

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs

import com.miguel.jobharbor2.R
import com.miguel.jobharbor2.data.entity.Notas
import com.miguel.jobharbor2.databinding.FragmentEditarNotasBinding
import com.miguel.jobharbor2.viewModel.NotasViewModel

import kotlinx.coroutines.launch

class editarNotas : Fragment(R.layout.fragment_editar_notas) {


    val viewModel by viewModels<NotasViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val binding = FragmentEditarNotasBinding.bind(requireView())
        val args: editarNotasArgs by navArgs()
        val note = args.notas

        if (note !== null) {
            Log.i("MyActivity", "Modificar contenido")
            Log.i("MyActivity", note.toString())
            //modificar contenido
            binding.apply {
                titleEdit.setText(note.titulo)
                contentEdit.setText(note.contenido)
                saveBtn.setOnClickListener {
                    val id = note.ID
                    val titulo = titleEdit.text.toString()
                    val contenido = contentEdit.text.toString()
                    val fecha = System.currentTimeMillis()
                    val nuevanota = Notas(id, titulo, contenido, fecha)

                    //añadir a la base de datos

                    lifecycleScope.launch {
                        try {
                            Log.i("MyActivity", "Añadido")
                            viewModel.update(nuevanota)
                            AlertDialog.Builder(it.context).setMessage("Nota actualizada")
                                .setPositiveButton("OK", null).show()

                        } catch (e: Exception) {
                            Log.i("MyActivity", "ERROR  ${e}")
                            AlertDialog.Builder(it.context).setMessage("Error durante la inserción")
                                .setPositiveButton("OK", null).show()
                        }
                    }


                }

            }
        } else {
            Log.i("MyActivity", "nuevo")
            //crear uno nuevo
            binding.apply {
                saveBtn.setOnClickListener {
                    Log.i("MyActivity", generarCadenaAleatoria())

                    val id = generarCadenaAleatoria()
                    val titulo = titleEdit.text.toString()
                    val contenido = contentEdit.text.toString()
                    val fecha = System.currentTimeMillis()
                    val nuevanota = Notas(id.toInt(), titulo, contenido, fecha)


                    AlertDialog.Builder(it.context)
                        .setMessage("Guardado en la base de datos" + titulo)
                        .setPositiveButton("OK", null).show()

                    viewModel.insert(nuevanota)
                }
            }
        }


    }

    fun generarCadenaAleatoria(longitud: Int = 8): String {
        val caracteresPermitidos = "0123456789"
        return (1..longitud)
            .map { caracteresPermitidos.random() }
            .joinToString("")
    }


}