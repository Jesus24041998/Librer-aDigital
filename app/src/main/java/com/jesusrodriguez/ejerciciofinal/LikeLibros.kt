/*
 * Copyright (c) 2021. Project made by Jesús Rodríguez Malagón
 */

package com.jesusrodriguez.ejerciciofinal

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.runBlocking

/**
 * Clase LikeLibros se encarga de agregar una lista de libros los cuales el usuario a agregado like a un nuevo RecyclerView
 */
private var toolbar: Toolbar? = null
private var recyclerView: RecyclerView? = null
private var array: ArrayList<Libro>? = null
private var context : Context? = null
class LikeLibros : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_like_libro)
        referenciarObjetos()
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setTitle(resources.getString(R.string.listalikes))
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        var loadPreferences: SharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
        val usuario = loadPreferences.getString("user", "")
        runBlocking {
            array = LibroSQL(context).recogerLibrosLike(usuario)
        }
        conectarAdaptadorConRecyclerView(array!!)
    }
    private fun referenciarObjetos() {
        toolbar = findViewById(R.id.toolb)
        recyclerView = findViewById(R.id.listarecicladalibroslike)
        array = ArrayList()
        context = this
    }
    //Función que carga el adaptador al recyclerView
    private fun conectarAdaptadorConRecyclerView(arrayList: ArrayList<Libro>) {
        var llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        var adaptador = AdaptadorRecyclerViewLikes(arrayList, this)
        recyclerView?.layoutManager = llm
        recyclerView?.adapter = adaptador
    }
    //Método que nos devuelve a la actividad anterior
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}