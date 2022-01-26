/*
 * Copyright (c) 2021. Project made by Jesús Rodríguez Malagón
 */

package com.jesusrodriguez.ejerciciofinal

/**
 * Clase Usuario que carga un usuario , usada para loguear o registrar un objeto Usuario
 */
class Usuario {
    private var identificador: String?
    private var correo: String?
    private var contrasena: String?

    constructor(identificador: String?, correo: String?, contrasena: String?) {
        this.identificador = identificador
        this.correo = correo
        this.contrasena = contrasena
    }

    fun getIdentificador(): String? {
        return identificador
    }

    fun setIdentificador(indentificador: String?) {
        this.identificador = indentificador
    }

    fun getCorreo(): String? {
        return correo
    }

    fun getContrasena(): String? {
        return contrasena
    }

    fun setCorreo(correo: String) {
        this.correo = correo
    }

}