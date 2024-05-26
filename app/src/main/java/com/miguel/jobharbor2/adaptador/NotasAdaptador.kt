package com.miguel.jobharbor2.adaptador

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.miguel.jobharbor2.data.entity.Notas
import com.miguel.jobharbor2.databinding.DatosNotasBinding
import com.miguel.jobharbor2.iu.notasFragmento
import java.text.SimpleDateFormat


class NotasAdaptador(private val mNotes: List<Notas>, private val listener: OnNoteClickListener) :
    RecyclerView.Adapter<NotasAdaptador.ViewHolder>() {
    interface OnNoteClickListener {
        fun onNoteClick(note: Notas)
        fun onNoteLongClick(note: Notas)
    }

    inner class ViewHolder(private val binding: DatosNotasBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val nota = mNotes[position]
                        listener.onNoteClick(nota)
                    }
                }
                root.setOnLongClickListener {
                    val position = adapterPosition

                    if (position != RecyclerView.NO_POSITION) {
                        val nota = mNotes[position]


                        mostrarAlertaConOpciones(it.context, "Alerta", "¿Estás seguro de que deseas continuar?", accionAceptar = {
                            // Lógica que se ejecutará al hacer clic en "Aceptar"
                            // Por ejemplo, puedes realizar alguna acción o navegar a otra pantalla
                            // Por ejemplo:
                            // startActivity(Intent(this, OtraActividad::class.java))
                            listener.onNoteLongClick(nota)
                        })









                    }
                    true
                }
            }
        }

        fun bind(nota: Notas) {
            binding.apply {

                titleNote.text = nota.titulo
                contentNote.text = nota.contenido
                val formatter = SimpleDateFormat("dd/MM/yyyy")
                dateNote.text = formatter.format(nota.fecha)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DatosNotasBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(mNotes[position]) {
            holder.bind(this)
        }
    }

    override fun getItemCount(): Int {
        return mNotes.size
    }

    fun mostrarAlertaConOpciones(
        context: Context,
        titulo: String,
        mensaje: String,
        accionAceptar: () -> Unit
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(titulo)
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            accionAceptar() // Llama a la función de acción al hacer clic en "Aceptar"
            dialog.dismiss() // Cierra la alerta
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss() // Cierra la alerta si se hace clic en "Cancelar"
        }
        val alerta = builder.create()
        alerta.show()
    }

}