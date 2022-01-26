/*
 * Copyright (c) 2021. Project made by Jesús Rodríguez Malagón
 */

package com.jesusrodriguez.ejerciciofinal

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window

/**
 * Clase SplashScreen que se dedica a agregar una Splash Screen a nuestra app , lo que le da un toque más profesional.
 * Segun si el usuario esta logueado o no , nos abre una actividad u otra.
 */
private const val SPLASH_SCREEN_DELAY: Long = 2000
private var usuario :String? = null
private var context :Context? = null
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var loadPreferences: SharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
        usuario = loadPreferences.getString("user", "")
        context = this
        // Orientacion de la actividad
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        // Oculto la barra de titulo
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.splash_screen)
        cargarApp()
    }
    private fun cargarApp()
    {
        Handler().postDelayed(object : Runnable {
            override fun run() {

                // Se inicia la actividad principal de la aplicación
                if (usuario=="" ) {
                    val intent = Intent(context,LoginActivity::class.java)
                    startActivity(intent)
                }
                else if(usuario != "")
                {
                    val intent = Intent(context,MainApp::class.java)
                    intent.putExtra("likes",true)
                    startActivity(intent)
                }
                finish()
            }
        }, SPLASH_SCREEN_DELAY)
    }


}