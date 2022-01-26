/*
 * Copyright (c) 2021. Project made by Jesús Rodríguez Malagón
 */

package com.jesusrodriguez.ejerciciofinal

import android.content.Context
import android.graphics.Bitmap
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.setThreadPolicy
import android.os.SystemClock
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.loader.content.AsyncTaskLoader
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * Clase LibroSQL , se encarga de todas las funciones en la base de datos , de conexión con esta y de recogida , inserción, actualización y eliminación de datos.
 */
class LibroSQL {
    //PHP server
    companion object {

        //En localhost todas las funcionalidades de la app son correctas , el server es más lento
        //por tanto la carga de trabajo sobre agregar los likes a los libros etc puede diferir
        //para un mayor testeo de las funcionalidades usar un servidor local e insertar la base de datos predefinida por defecto
        //comenta y descomenta para cambiar el servidor , por defecto viene configurado para internet
        private val URLSERVIDOR = "https://libreriadigitallike.000webhostapp.com/"

        //private val URLSERVIDOR = "http://192.168.0.2/"
        private val urlregistrousuario = URLSERVIDOR + "registrarUsuario.php"
        private val urlregistrolibro = URLSERVIDOR + "registrarLibro.php"
        private val recogerusuario = URLSERVIDOR + "obtenerUsuario.php"
        private val recogerlibros = URLSERVIDOR + "obtenerLibros.php"
        private val urlregistrolike = URLSERVIDOR + "registrarLike.php"
        private val recogerlike = URLSERVIDOR + "recogerLike.php"
        private val recogerusuariolike = URLSERVIDOR + "recogerLikeUsuario.php"
        private val recogerlibrosegunlike = URLSERVIDOR + "obtenerLibrosSegunLike.php"
    }

    //Variables clase
    private var objectbytearrayOutputStream: ByteArrayOutputStream? = null
    private var imageinbyte: ByteArray? = null
    private var context: Context? = null
    private var policy: ThreadPolicy? = null

    constructor(
        context: Context?,
    ) {
        this.objectbytearrayOutputStream = ByteArrayOutputStream()
        this.imageinbyte = imageinbyte
        this.context = context
        this.policy = ThreadPolicy.Builder().permitAll().build()
        setThreadPolicy(policy)
    }

    //Función que se encarga de agregar like a un libro
    fun insertarLike(idusuario: String?, idlibro: String?, like: Boolean?): Boolean? {

        var registrado = false
        var parser = JSONParser()
        var parametros: HashMap<String, String> = HashMap()
        parametros.put("idusuario", idusuario!!)
        parametros.put("idlibro", idlibro!!)
        parametros.put("like", like.toString()!!)

        try {

            var array: JSONArray = parser.getJSONArrayFromUrl(urlregistrolike, parametros)
            var resultado = array.getJSONObject(0).getString("registrado")
            if (resultado.equals("ok")) registrado = true else registrado = false

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return registrado
    }

    //Función que se encarga de agregar un usuario a nuestra app
    fun insertarUsuario(correo: String, contrasena: String): Boolean {
        var registrado = false
        var parser = JSONParser()
        var parametros: HashMap<String, String> = HashMap()
        parametros.put("correo", correo)
        parametros.put("contraseña", contrasena)
        try {
            var array: JSONArray = parser.getJSONArrayFromUrl(urlregistrousuario, parametros)
            var resultado = array.getJSONObject(0).getString("registrado")
            if (resultado.equals("ok")) registrado = true else registrado = false

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return registrado
    }

    //Función que se encarga de recoger un usuario
    fun seleccionarUsuario(correo: String, contrasena: String): Usuario {

        val parser = JSONParser()
        var datos: JSONArray? = null
        var objeto: JSONObject
        var usuario: Usuario? = null
        val parametros = HashMap<String, String>()
        parametros.put("correo", correo)
        parametros.put("contraseña", contrasena)

        try {

            datos = parser.getJSONArrayFromUrl(recogerusuario, parametros)

            objeto = datos!!.getJSONObject(0)
            if (objeto.getString("identificador").equals("")) {
                Toast.makeText(context, "Datos incorrectos", Toast.LENGTH_LONG).show()
                usuario = Usuario(null, null, null)
            } else usuario =
                Usuario(objeto.getString("identificador"), objeto.getString("correo"), null)
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return usuario!!
    }

    //Esta es la clase asincrona que se encarga de recoger los libros.
    class TaskRecibirLibros : AsyncTaskLoader<ArrayList<Libro>> {
        private var progressbar: ProgressBar?

        constructor(
            context: Context,
            progressbar: ProgressBar?
        ) : super(context) {
            this.progressbar = progressbar
        }

        override fun onStartLoading() {
            progressbar?.visibility = View.VISIBLE
            forceLoad()
        }

        override fun loadInBackground(): ArrayList<Libro>? {
            var arraylibro = ArrayList<Libro>()
            val parser = JSONParser()
            var datos: JSONArray? = null
            var objeto: JSONObject

            try {
                datos = parser.getJSONArrayFromUrl(recogerlibros, null)
                var numero = datos!!.length() - 1
                for (i in 0..numero) {
                    objeto = datos!!.getJSONObject(i)

                    try {

                        var libro = Libro(
                            objeto.getInt("id"),
                            null,
                            objeto.getString("titulo"),
                            objeto.getString("autor"),
                            objeto.getInt("likes")
                        )

                        if (objeto.getString("id").equals("")) arraylibro.add(
                            Libro(
                                null,
                                null,
                                null,
                                null,
                                null
                            )
                        ) else arraylibro.add(libro)
                    } catch (a: java.lang.Exception) {
                    }
                }

            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            SystemClock.sleep(3000)
            return arraylibro
        }

        override fun deliverResult(data: ArrayList<Libro>?) {
            super.deliverResult(data)
        }

        override fun onStopLoading() {
            cancelLoad()
        }

        override fun onReset() {
            super.onReset()
            onStopLoading()
        }
    }

    //Función que se encarga de recibir los likes de un libro
    fun recibirLike(titulo: String): String? {
        var like: Int? = null
        val parser = JSONParser()
        var datos: JSONArray? = null
        var objeto: JSONObject
        var parametros: HashMap<String, String> = HashMap()
        try {
            parametros.put("titulo", titulo)

            datos = parser.getJSONArrayFromUrl(recogerlike, parametros)

            objeto = datos!!.getJSONObject(0)
            like = objeto.getInt("likes")
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return like.toString()!!
    }

    //Función innecesaria
    fun insertarLibro(libro: Libro): Boolean {
        var registrado = false
        var parser = JSONParser()
        var parametros: HashMap<String, String> = HashMap()
        //Conversor de imagenes
        var imagen = libro.getImage()
        val baos = ByteArrayOutputStream()
        imagen?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val urlimagen = "imagenes/".plus(libro.getTitulo()).plus(".jpg")

        parametros.put("titulo", libro.getTitulo()!!)
        parametros.put(
            "caratula", urlimagen
        )
        parametros.put("autor", libro.getAutor()!!)

        FirebaseStorage(context).insertarImagenesenFirebaseStorage(urlimagen, data)
        try {
            var array: JSONArray = parser.getJSONArrayFromUrl(urlregistrolibro, parametros)
            var resultado = array.getJSONObject(0).getString("registrado")

            if (resultado.equals("ok")) registrado = true else registrado = false

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return registrado
    }

    //Función que se encarga de recoger los libros a los cuales el usuario logueado le ha dado like
    fun recogerLibrosLike(uidUsuario: String?): ArrayList<Libro> {

        var arraylibro = ArrayList<Libro>()
        val parser = JSONParser()
        var datos: JSONArray? = null
        var objeto: JSONObject
        var parametros: HashMap<String, String> = HashMap()
        try {
            parametros.put("idusua", uidUsuario!!)
            datos = parser.getJSONArrayFromUrl(recogerlibrosegunlike, parametros)
            var numero = datos!!.length() - 1
            for (i in 0..numero) {
                objeto = datos!!.getJSONObject(i)
                try {

                    var libro = Libro(
                        null,
                        null,
                        objeto.getString("titulo"),
                        objeto.getString("autor"),
                        null
                    )

                    if (objeto.getString("titulo").equals("")) arraylibro.add(
                        Libro(
                            null,
                            null,
                            null,
                            null,
                            null
                        )
                    ) else arraylibro.add(libro)
                } catch (a: java.lang.Exception) {
                }
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return arraylibro

    }

    //Función que se encarga de saber si el boton de like esta marcado o no.
    fun botonLike(uidUsuario: String, idlibro: Int): Boolean {
        var like: Boolean? = null
        val parser = JSONParser()
        var datos: JSONArray? = null
        var objeto: JSONObject
        var parametros: HashMap<String, String> = HashMap()

        try {
            parametros.put("idusuario", uidUsuario!!)
            parametros.put("idlibro", idlibro.toString())

            datos = parser.getJSONArrayFromUrl(recogerusuariolike, parametros)

            objeto = datos!!.getJSONObject(0)
            like = objeto.getBoolean("resultado")
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return like!!
    }
}