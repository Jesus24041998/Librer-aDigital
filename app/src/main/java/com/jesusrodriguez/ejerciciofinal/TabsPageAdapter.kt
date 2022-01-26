/*
 * Copyright (c) 2021. Project made by Jesús Rodríguez Malagón
 */

package com.jesusrodriguez.ejerciciofinal

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jesusrodriguez.ejerciciofinal.R

/**
 * La clase TabsPageAdapter se encarga de la funcionalidad de nuestra actividad tab dentro del loguin de nuestra app ,
 * es la encargada de agregar los titulos a las cabeceras de los tabs y a crear los fragmentos correspondientes
 */
class TabsPageAdapter(
    private val context: Context,
    fm: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fm, lifecycle) {


    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                val bundle = Bundle()
                bundle.putString("fragmentName", context.getString(R.string.registro))
                val register = DemoFragment()
                register.arguments = bundle
                return register
                true
            }
            1 -> {
                val bundle = Bundle()
                bundle.putString("fragmentName", context.getString(R.string.login))
                val login = DemoFragment()
                login.arguments = bundle
                return login
                true
            }
            else -> return DemoFragment()
        }

    }

    override fun getItemCount(): Int {
        return 2
    }
}