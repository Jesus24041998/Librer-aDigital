/*
 * Copyright (c) 2021. Project made by Jesús Rodríguez Malagón
 */

package com.jesusrodriguez.ejerciciofinal

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout

private var titulo: String? = null
private var autor: String? = null
private var bitarray: ByteArray? = null
private var imagen: Bitmap? = null
private var modo: String? = null
private var borde: LinearLayout? = null

//Elementos Vista
private var tit: TextView? = null
private var aut: TextView? = null
private var ttit: TextView? = null
private var taut: TextView? = null
private var ima: ImageView? = null
private var botonretroceso: ImageButton? = null
private var titulopagina: TextView? = null

//Vistas para ocultar
private var like: ToggleButton? = null
private var nlike: TextView? = null

/**
 * Clase Info_Libro , se encarga de mostrar una nueva activity al pulsar un item del recycler view y de cargar todos los datos
 */
class Info_Libro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_libro)
        //Recogida de información

        titulo = intent.getStringExtra("titulo")
        autor = intent.getStringExtra("autor")
        bitarray = intent.getByteArrayExtra("imagen")
        //De bitarray a bitmap
        imagen = BitmapFactory.decodeByteArray(bitarray, 0, bitarray!!.size)
        modo = intent.getStringExtra("modo")
        cargarVistas()
        cargarElementosPagina(titulo, autor, imagen)
        botonretroceso?.setOnClickListener {
            finish()
        }
    }

    private fun cargarVistas() {
        tit = findViewById(R.id.titulolibro)
        ttit = findViewById(R.id.titulo)
        aut = findViewById(R.id.autorlibro)
        taut = findViewById(R.id.autor)
        ima = findViewById(R.id.imagenlibro)
        borde = findViewById(R.id.seleccion)
        titulopagina = findViewById(R.id.infotitulolibro)
        var string: String = this.getString(R.string.infotitulo).plus(" ").plus(titulo)
        titulopagina?.setText(string)
        botonretroceso = findViewById(R.id.botonretroceso)
        var include: ConstraintLayout? = findViewById(R.id.info)
        include?.visibility = View.VISIBLE
        like = findViewById(R.id.like)
        nlike = findViewById(R.id.nlikes)
        like?.visibility = View.GONE
        nlike?.visibility = View.GONE
    }


    private fun cargarElementosPagina(titulo: String?, autor: String?, imagen: Bitmap?) {
        tit?.setText(titulo)
        aut?.setText(autor)

        ima?.setImageBitmap(imagen)
        if (modo.equals("Claro")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            tit?.setTextColor(Color.BLACK)
            ttit?.setTextColor(Color.BLACK)
            aut?.setTextColor(Color.BLACK)
            taut?.setTextColor(Color.BLACK)
            titulopagina?.setTextColor(Color.BLACK)
            botonretroceso?.setImageResource(R.drawable.back_oscuro)
            borde?.setBackgroundResource(R.drawable.borde_oscuro)
        } else if (modo.equals("Oscuro")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            tit?.setTextColor(Color.WHITE)
            ttit?.setTextColor(Color.WHITE)
            aut?.setTextColor(Color.WHITE)
            taut?.setTextColor(Color.WHITE)
            titulopagina?.setTextColor(Color.WHITE)
            botonretroceso?.setImageResource(R.drawable.back_claro)
            borde?.setBackgroundResource(R.drawable.borde_claro)
        }
    }
}
