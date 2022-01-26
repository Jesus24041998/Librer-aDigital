/*
 * Copyright (c) 2021. Project made by Jesús Rodríguez Malagón
 */

package com.jesusrodriguez.ejerciciofinal

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

/**
 * Clase LoginActivity se encarga de recoger el tab que esta seleccionado , y cargar la vista de usuario.
 */
class LoginActivity : AppCompatActivity() {
    private var tema = false
    private var botonmodo: ImageButton? = null
    private var tabBar: TabLayout? = null
    private var view_pager: ViewPager2? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        declaracionVariables()
        comprobarModo()
        val adapter = TabsPageAdapter(this, supportFragmentManager, lifecycle)
        view_pager?.adapter = adapter
        tabBar?.tabMode = TabLayout.MODE_FIXED
        TabLayoutMediator(tabBar!!, view_pager!!) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = resources.getText(R.string.registro)
                    true
                }
                1 -> {
                    tab.text = resources.getText(R.string.login)
                    true
                }
            }
        }.attach()
        botonmodo?.setOnClickListener {
            tema = !tema
            estadoModo(tema)
        }

    }

    private fun declaracionVariables() {
        botonmodo = findViewById(R.id.botonmodo)
        tabBar = findViewById(R.id.tabs)
        view_pager = findViewById(R.id.view_pager)
    }

    private fun comprobarModo() {
        var loadPreferences: SharedPreferences = getSharedPreferences("config", MODE_PRIVATE)
        var tema = loadPreferences.getBoolean("theme", false)
        estadoModo(tema)
    }

    @SuppressLint("ResourceType")
    private fun estadoModo(tema: Boolean) {
        var savePreferences: SharedPreferences = getSharedPreferences("config", MODE_PRIVATE)
        var objeditor: SharedPreferences.Editor = savePreferences.edit()
        objeditor.putBoolean("theme", tema)
        objeditor.commit()
        var id: Int? = null
        if (tema) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            id = R.drawable.moon
            tabBar?.setSelectedTabIndicator(R.color.white)
            tabBar?.tabTextColors = getColorStateList(R.color.white)

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            id = R.drawable.sun
            tabBar?.setSelectedTabIndicator(R.color.black)
            tabBar?.tabTextColors = getColorStateList(R.color.black)
        }
        botonmodo?.setBackgroundResource(id)
        //Cambia el tema de la aplicación


    }
}