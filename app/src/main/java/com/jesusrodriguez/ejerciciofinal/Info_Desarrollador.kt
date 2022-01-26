/*
 * Copyright (c) 2021. Project made by Jesús Rodríguez Malagón
 */

package com.jesusrodriguez.ejerciciofinal

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar

private var info: TextView? = null
private var toolbar: Toolbar? = null

/**
 * Clase Info_Desarrollador crea una nueva activity que muestra un poco de información del proyecto y del desarrollador
 */
class Info_Desarrollador : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_desarrollador)
        referenciar()
        estado()
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
    }

    @SuppressLint("ResourceAsColor")
    private fun estado() {
        var loadPreferences: SharedPreferences = getSharedPreferences("config", MODE_PRIVATE)
        var tema = loadPreferences.getBoolean("theme", false)
        if (tema) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            info?.setTextColor(Color.WHITE)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            info?.setTextColor(Color.BLACK)
        }
    }

    private fun referenciar() {
        info = findViewById(R.id.textoinfo)
        toolbar = findViewById(R.id.toolba)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}