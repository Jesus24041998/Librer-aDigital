/*
 * Copyright (c) 2021. Project made by Jesús Rodríguez Malagón
 */

package com.jesusrodriguez.ejerciciofinal

import android.graphics.Bitmap

/**
 * Clase Libro , usada para recoger o registrar un objeto Libro
 */
class Libro {
    private var id : Int?
    private var imagen: Bitmap?
    private var titulo: String?
    private var autor: String?
    private var likes: Int?

    constructor(id:Int?,imagen: Bitmap?, titulo: String?, autor: String?,likes:Int?) {
        this.id = id
        this.imagen = imagen
        this.titulo = titulo
        this.autor = autor
        this.likes = likes
    }
    fun getId():Int?
    {
        return id
    }
    fun getImage(): Bitmap? {
        return imagen
    }

    fun getTitulo(): String? {
        return titulo
    }

    fun getAutor(): String? {
        return autor
    }
    fun getLikes(): Int? {
        return likes
    }
    fun setAutor(autor:String) {
        this.autor =autor
    }
    fun setTitulo(titulo:String) {
        this.titulo =titulo
    }
    fun setCaratula(imagen:Bitmap) {
        this.imagen =imagen
    }
    fun setLikes(likes:Int) {
        this.likes =likes
    }
}