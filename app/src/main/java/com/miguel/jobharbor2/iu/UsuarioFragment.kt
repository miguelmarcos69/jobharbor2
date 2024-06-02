package com.miguel.jobharbor2.iu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.miguel.jobharbor2.R
import com.miguel.jobharbor2.data.entity.Usuario
import com.miguel.jobharbor2.databinding.FragmentUsuarioBinding
import com.miguel.jobharbor2.viewModel.UsuarioViewModel
import kotlinx.coroutines.launch

class UsuarioFragmento : Fragment(R.layout.fragment_usuario) {
    // TODO: Rename and change types of parameters
    val viewModel by viewModels<UsuarioViewModel>()
    lateinit var usuarioCargado: Usuario
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentUsuarioBinding.bind(requireView())
        Log.i("usuario", "entro")

        binding.apply {


            viewLifecycleOwner.lifecycleScope.launch {

            }

        }


        val userJsonList = viewModel.getUser(requireContext())

        if (userJsonList != null) {
            for (json in userJsonList) {
                binding.nombre.setText(json.nombre)
                binding.ape.setText(json.ape)
                binding.email.setText(json.email)
                binding.contrasena.setText(json.contra)
                usuarioCargado =
                    Usuario(json.ID, json.nombre, json.ape, json.contra, json.email)
                Log.i("usu", "JSON del usuario: ${json.email}")
            }
        } else {
            Log.i("usu", "La lista de usuarios es null")
        }

        binding.guardarBoton.setOnClickListener {

            val id = usuarioCargado.ID
            val nombre = binding.nombre.text.toString()
            val ape = binding.ape.text.toString()
            val email = binding.email.text.toString()
            val contra = binding.contrasena.text.toString()
            val nuevoUsu = Usuario(id, nombre, ape, contra, email)
            Log.i("usu", "Nuevo usuario: $nuevoUsu")

            Log.i("usuario cargado", nuevoUsu.toString())
            viewLifecycleOwner.lifecycleScope.launch {

                viewModel.update(nuevoUsu)
                viewModel.usuario = nuevoUsu
                Log.i("usuario", viewModel.usuario!!.nombre.toString())

            }

        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_usuario, container, false)
    }


}