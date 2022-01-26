/*
 * Copyright (c) 2021. Project made by Jesús Rodríguez Malagón
 */

package com.jesusrodriguez.ejerciciofinal

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
/**
 * Clase adaptador encargada de mostrar nuestros items que el usuario a agregado como likes
 */
class AdaptadorRecyclerViewLikes(
    private var array: ArrayList<Libro>?,
    private var contexto: Context,
) :
    RecyclerView.Adapter<AdaptadorRecyclerViewLikes.ViewHolderLibros>() {
    var arraylibros: ArrayList<Libro>? = null

    init {
        arraylibros?.clear()
        this.arraylibros = array
        this.contexto = contexto
    }

    class ViewHolderLibros(view: View) : RecyclerView.ViewHolder(view) {
        //Declarando elementos del recyclerView
        var imagen: ImageView
        var titulo: TextView
        var autor: TextView
        var ttitulo: TextView
        var tautor: TextView
        var info: ConstraintLayout
        val storageReference = Firebase.storage.reference
        var seleccion: LinearLayout
        init {
            imagen = view.findViewById(R.id.imagenlibro)
            titulo = view.findViewById(R.id.titulolibro)
            autor = view.findViewById(R.id.autorlibro)
            ttitulo = view.findViewById(R.id.titulo)
            tautor = view.findViewById(R.id.autor)
            info = view.findViewById(R.id.info)
            seleccion = view.findViewById(R.id.seleccion)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderLibros {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_libro, parent, false)
        return ViewHolderLibros(view)
    }

    override fun onBindViewHolder(holder: ViewHolderLibros, position: Int) {
        holder.info.visibility = View.GONE
        if (position < 0) {
            agregarLibroVacio()
            holder.imagen.setImageBitmap(array?.get(0)?.getImage())
            holder.titulo.text = array?.get(0)?.getTitulo()
            holder.autor.text = array?.get(0)?.getAutor()
        } else {
            //Cambios en el darkmode
            var modo: String? = null

            holder.titulo.text = array?.get(position)?.getTitulo()

            //Insertar imagen desde Firebase
            var image = holder.storageReference.child(
                "imagenes/".plus(array?.get(position)?.getTitulo()).plus(".jpg")
            )
            FirebaseAPP(contexto)
            var auth: FirebaseAuth
            auth = Firebase.auth
            val currentUser = auth.currentUser
            if (currentUser != null) {

                GlideApp.with(holder.imagen.context)
                    .load(image)
                    .into(holder.imagen)

            }
            holder.autor.text = array?.get(position)?.getAutor()
//Ejecución del like
            var comprobacion = AppCompatDelegate.getDefaultNightMode()
            if (comprobacion == 2) {
                modo = "Oscuro"

            } else {
                modo = "Claro"
            }

            when (comprobacion) {
                1 -> {
                    holder.titulo.setTextColor(Color.BLACK)
                    holder.ttitulo.setTextColor(Color.BLACK)
                    holder.autor.setTextColor(Color.BLACK)
                    holder.tautor.setTextColor(Color.BLACK)
                    holder.seleccion.setBackgroundResource(R.drawable.borde_oscuro)
                }
                2 -> {
                    holder.ttitulo.setTextColor(Color.WHITE)
                    holder.titulo.setTextColor(Color.WHITE)
                    holder.autor.setTextColor(Color.WHITE)
                    holder.tautor.setTextColor(Color.WHITE)
                    holder.seleccion.setBackgroundResource(R.drawable.borde_claro)
                }
            }
        }
    }

    override fun getItemCount() = array!!.size

    private fun agregarLibroVacio(): Libro {
        return Libro(null, null, null, null, null)
    }

    fun refrescar() {
        notifyDataSetChanged()
    }
}