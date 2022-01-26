/*
 * Copyright (c) 2021. Project made by Jesús Rodríguez Malagón
 */
package com.jesusrodriguez.ejerciciofinal

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * La clase "principal" MainApp de nuestra aplicación , hereda de otra clase LoaderManager que permite agregar Asincronía a una clase y gracias a esta conectamos
 * esta clase asincrona que se encuentra en la clase LibroSQL
 * Esta clase tambien llama a la clase AdaptadorRecyclerView para agregar los items de la base de datos a dicha lista
 */
//Variables
private var toolbar: Toolbar? = null
private var boton: FloatingActionButton? = null
private var tema = false
private var recyclerView: RecyclerView? = null
private var array: ArrayList<Libro>? = null
private var likebutton: ToggleButton? = null
private var auth: FirebaseAuth? = null
private var currentUser: FirebaseUser? = null
private var context: Context? = null
private var progressbar: ProgressBar? = null

class MainApp : AppCompatActivity(), LoaderManager.LoaderCallbacks<ArrayList<Libro>> {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_app)
        //Instanciamos la toolbar
        referenciarObjetos()
        setSupportActionBar(toolbar)
        toolbar?.setTitle(R.string.app_name)
        //Para evitar un bug de que salte la asincronía y el recicler ya esté cargado agregamos una condición que viene del splashscreen
        var intent = intent.getBooleanExtra("likes", false)
        if (intent) {
            //LoaderManager que crea la instancia
            LoaderManager.getInstance(this).initLoader(0, null, this)
        }
    }

    //Crea el loader con la clase TaskRecibirLibros dentro de LibroSQL
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<ArrayList<Libro>> {
        return LibroSQL.TaskRecibirLibros(this, progressbar)
    }
    //Cuando termina esta tarea devuelve como data el array con los libros y llamamos a la funcion de conexion del adaptador al recycler view
    override fun onLoadFinished(loader: Loader<ArrayList<Libro>>, data: ArrayList<Libro>?) {
        array = data!!
        progressbar?.visibility = View.GONE
        conectarAdaptadorConRecyclerView(array!!)
    }

    override fun onLoaderReset(loader: Loader<ArrayList<Libro>>) {
        loader.reset()
    }


    private fun referenciarObjetos() {
        toolbar = findViewById(R.id.tool)
        boton = findViewById(R.id.cambiarmodo)
        recyclerView = findViewById(R.id.listarecicladalibros)
        array = ArrayList()
        likebutton = findViewById(R.id.like)
        auth = Firebase.auth
        currentUser = auth?.currentUser
        context = this
        progressbar = findViewById(R.id.progressBarRecicler)
    }
    //Creamos un menu de opciones y comprobamos si la aplicación esta en modo oscuro o claro
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menutoolbar, menu)
        var loadPreferences: SharedPreferences = getSharedPreferences("config", MODE_PRIVATE)
        var tema = loadPreferences.getBoolean("theme", false)
        estadoModo(tema, menu?.findItem(R.id.cambiarmodo)!!)
        return super.onCreateOptionsMenu(menu)
    }
    //Métpdp que comprueba el modo de la app
    private fun estadoModo(tema: Boolean, menu: MenuItem) {
        var savePreferences: SharedPreferences = getSharedPreferences("config", MODE_PRIVATE)
        var objeditor: SharedPreferences.Editor = savePreferences.edit()
        objeditor.putBoolean("theme", tema)
        objeditor.commit()

        val id = if (tema) R.drawable.moon else R.drawable.sun
        menu.icon = ContextCompat.getDrawable(this, id)
        //Cambia el tema de la aplicación
        if (tema) {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            AdaptadorRecyclerView(null, this).refrescar()
        } else {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
            AdaptadorRecyclerView(null, this).refrescar()
        }

    }
    //Utilización del menu de opciones
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.cambiarmodo -> {
                tema = !tema
                estadoModo(tema, item)
                true
            }

            R.id.listalikes -> {
                var intent = Intent(context, LikeLibros::class.java)
                startActivity(intent)
                true
            }
            R.id.info -> {
                var intent = Intent(context, Info_Desarrollador::class.java)
                startActivity(intent)
                true
            }
            R.id.cerrar -> {
                val builder = AlertDialog.Builder(context!!)
                builder.setMessage("¿Está seguro?")
                builder.setPositiveButton(R.string.yes) { dialog, which ->
                    auth?.signOut()

                    var savePreferences: SharedPreferences =
                        context!!.getSharedPreferences(
                            "user",
                            AppCompatActivity.MODE_PRIVATE
                        )
                    var objeditor: SharedPreferences.Editor = savePreferences.edit()
                    objeditor.putString("user", "")
                    objeditor.commit()
                    finish()
                    var intent = Intent(context, SplashScreenActivity::class.java)
                    startActivity(intent)
                }

                builder.setNegativeButton(R.string.no) { dialog, which ->
                }
                builder.show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
    //Cerramos la app si presionamos el boton de volver
    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }
    //Conecta el adaptador con el recicler
    private fun conectarAdaptadorConRecyclerView(arrayList: ArrayList<Libro>) {
        var llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        var adaptador = AdaptadorRecyclerView(arrayList, this)
        recyclerView?.layoutManager = llm
        recyclerView?.adapter = adaptador
    }


}


