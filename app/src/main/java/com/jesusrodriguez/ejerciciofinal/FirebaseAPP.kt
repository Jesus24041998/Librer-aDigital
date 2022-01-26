/*
 * Copyright (c) 2021. Project made by Jesús Rodríguez Malagón
 */

package com.jesusrodriguez.ejerciciofinal

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory

/**
 * Clase FirebaseAPP sirve para inicializar Firebase
 */
class FirebaseAPP {
    private val context: Context?
    private val firebaseAppCheck: FirebaseAppCheck?

    constructor(context: Context?) {
        this.context = context
        FirebaseApp.initializeApp(context!!)
        firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            SafetyNetAppCheckProviderFactory.getInstance()
        )
    }
}