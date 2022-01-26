/*
 * Copyright (c) 2021. Project made by Jesús Rodríguez Malagón
 */

package com.jesusrodriguez.ejerciciofinal

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

/**
 * Clase adaptador encargada de mostrar nuestros items en una lista reciclada y nos permite seleccionar estos items
 */
class AdaptadorRecyclerView(
    private var array: ArrayList<Libro>?,
    private var contexto: Context
) :
    RecyclerView.Adapter<AdaptadorRecyclerView.ViewHolderLibros>() {
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
        var seleccion: LinearLayout
        var ttitulo: TextView
        var tautor: TextView
        var info: ConstraintLayout
        var numerolikes: TextView
        var likebutton: ToggleButton
        var numeroid: TextView
        val storageReference = Firebase.storage.reference
        var context:Context
        var image : StorageReference?
        init {
            imagen = view.findViewById(R.id.imagenlibro)
            titulo = view.findViewById(R.id.titulolibro)
            autor = view.findViewById(R.id.autorlibro)
            seleccion = view.findViewById(R.id.seleccion)
            ttitulo = view.findViewById(R.id.titulo)
            tautor = view.findViewById(R.id.autor)
            info = view.findViewById(R.id.info)
            numerolikes = view.findViewById(R.id.nlikes)
            numerolikes.visibility = View.VISIBLE
            likebutton = view.findViewById(R.id.like)
            likebutton.visibility = View.VISIBLE
            numeroid = view.findViewById(R.id.nid)
            numeroid.visibility = View.VISIBLE
            context = view.context
            image = null
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderLibros {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_libro, parent, false)
        return ViewHolderLibros(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolderLibros,
        @SuppressLint("RecyclerView") position: Int
    ) {

        holder.info.visibility = View.GONE
        if (position < 0) {
            agregarLibroVacio()
            holder.imagen.setImageBitmap(array?.get(0)?.getImage())
            holder.titulo.text = array?.get(0)?.getTitulo()
            holder.autor.text = array?.get(0)?.getAutor()
        } else {
            //Cambios en el darkmode
            var modo: String? = null


            //Insertar imagen desde Firebase
            holder.image = holder.storageReference.child(
                "imagenes/".plus(array?.get(position)?.getTitulo()).plus(".jpg")
            )
            //Comprobamos que nuestro usuario de firebase esta logueado para evitar fallos a la hora de recoger dichas imagenes via Firebase
            FirebaseAPP(contexto)
            var auth: FirebaseAuth
            auth = Firebase.auth
            val currentUser = auth.currentUser
            if (currentUser != null) {
                holder.titulo.text = array?.get(position)?.getTitulo()
                holder.autor.text = array?.get(position)?.getAutor()
                holder.numeroid.text = array?.get(position)?.getId().toString()
                var likes = LibroSQL(contexto).recibirLike(holder.titulo.text.toString())
                holder.numerolikes.text = contexto.resources.getString(R.string.likes).plus(likes)

                //Usamos Firebase Storage, en concreto la descarga de imagenes con FirebaseUI
                GlideApp.with(holder.imagen!!.context)
                    .load(holder.image)
                    .into(holder.imagen!!)
            }

            //Comprobamos si esta puesto el modo nocturno
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
                    holder.numerolikes.setTextColor(Color.BLACK)
                    holder.numeroid.setTextColor(Color.BLACK)
                }
                2 -> {
                    holder.ttitulo.setTextColor(Color.WHITE)
                    holder.titulo.setTextColor(Color.WHITE)
                    holder.autor.setTextColor(Color.WHITE)
                    holder.tautor.setTextColor(Color.WHITE)
                    holder.numerolikes.setTextColor(Color.WHITE)
                    holder.seleccion.setBackgroundResource(R.drawable.borde_claro)
                    holder.numeroid.setTextColor(Color.WHITE)
                }
            }
            //Recogemos nuestro usuario
            var loadPreferences: SharedPreferences = contexto.getSharedPreferences(
                "user",
                AppCompatActivity.MODE_PRIVATE
            )
            var usuario = loadPreferences.getString("user", "")

            var botonlike =LibroSQL(contexto).botonLike(usuario!!, array?.get(position)?.getId()!!)

            //Comprobamos si la app esta en modo nocturno para agregar un tipo u otro de botón
            if (botonlike) {
                if (comprobacion == 2) {
                    holder.likebutton.setBackgroundResource(R.drawable.likeon_dark)
                } else {
                    holder.likebutton.setBackgroundResource(R.drawable.likeon)
                }

            } else {
                if (comprobacion == 2) {
                    holder.likebutton.setBackgroundResource(R.drawable.likeoff_dark)
                } else {
                    holder.likebutton.setBackgroundResource(R.drawable.likeoff)
                }
            }

            holder.likebutton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (comprobacion == 2) {
                        holder.likebutton.setBackgroundResource(R.drawable.likeon_dark)
                    } else {
                        holder.likebutton.setBackgroundResource(R.drawable.likeon)
                    }

                    var loadPreferences: SharedPreferences = contexto.getSharedPreferences(
                        "user",
                        AppCompatActivity.MODE_PRIVATE
                    )
                    var usuario = loadPreferences.getString("user", "")
                    //Este metodo agrega el like de dicho usuario a ese libro gracias al true
                    if (LibroSQL(contexto).insertarLike(
                            usuario,
                            holder.numeroid.text.toString(),
                            true
                        )!!
                    ) {
                        var titulo = holder.titulo.text.toString()
                        var numerolikes = LibroSQL(contexto).recibirLike(titulo)!!.toString()

                        holder.numerolikes.text =
                            contexto.resources.getString(R.string.likes).plus(numerolikes)
                        refrescar()
                    }

                } else {

                    if (comprobacion == 2) {
                        holder.likebutton.setBackgroundResource(R.drawable.likeoff_dark)
                    } else {
                        holder.likebutton.setBackgroundResource(R.drawable.likeoff)
                    }
                    var loadPreferences: SharedPreferences = contexto.getSharedPreferences(
                        "user",
                        AppCompatActivity.MODE_PRIVATE
                    )

                    var usuario = loadPreferences.getString("user", "")
                    //Este metodo elimina el like de dicho usuario a ese libro gracias al false
                    LibroSQL(contexto).insertarLike(
                        usuario,
                        holder.numeroid.text.toString(),
                        false
                    )

                    var titulo = holder.titulo.text.toString()
                    //Este metodo nos devuelve los likes de libro
                    var numerolikes = LibroSQL(contexto).recibirLike(titulo)!!.toString()

                    holder.numerolikes.text =
                        contexto.resources.getString(R.string.likes).plus(numerolikes)
                    refrescar()

                }
            }
            //Agregamos un onClick para cuando pulsamos en un item dentro de la lista nos muestre un Builder,
            // nos abre una nueva activity con un poco de información del item seleccionado.
            holder.seleccion.setOnClickListener {
                var string: String =
                    contexto.getString(R.string.dialog_mensaje).plus(" \"").plus(holder.titulo.text)
                        .plus("\" ").plus("?")

                //Bitmap to ByteArray
                var baos = ByteArrayOutputStream()
                refrescar()
                var image = holder.imagen.drawable
                var bitmap: Bitmap =
                    image.toBitmap(holder.imagen.width, holder.imagen.height, Bitmap.Config.RGB_565)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                var b = baos.toByteArray()


                val builder = AlertDialog.Builder(contexto)
                builder.setMessage(string)
                builder.setPositiveButton(R.string.yes) { dialog, which ->
                    //Abrir info libro
                    val intent = Intent(contexto, Info_Libro::class.java).apply {
                        putExtra("titulo", holder.titulo.text)
                        putExtra("autor", holder.autor.text)
                        putExtra("likes", holder.numerolikes.text)
                        putExtra("modo", modo)
                        putExtra("imagen", b)
                    }
                    startActivity(contexto, intent, null)

                }

                builder.setNegativeButton(R.string.no) { dialog, which ->

                }
                builder.show()
            }
        }
    }

    override fun getItemCount() = array!!.size

    private fun agregarLibroVacio(): Libro {
        return Libro(null, null, null, null, null)
    }
    //Refresca la lista
    fun refrescar() {
        notifyDataSetChanged()
    }

}