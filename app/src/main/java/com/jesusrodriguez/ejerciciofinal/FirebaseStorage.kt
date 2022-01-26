/*
 * Copyright (c) 2021. Project made by Jesús Rodríguez Malagón
 */

package com.jesusrodriguez.ejerciciofinal

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storageMetadata
import com.jesusrodriguez.ejerciciofinal.JSONParser.Companion.TAG
import java.net.URI

/**
 * Clase FirebaseStorage , es la encargada encargada de insertar las imagenes por defecto en nuestra app dentro de FirebaseStorage , no se usa comunmente en la app.
 */
class FirebaseStorage {
    private var context: Context? = null
    private var storage: FirebaseStorage? = null
    private var storageref: StorageReference? = null
    private var imageRef: StorageReference? = null
    private var uploadTask: UploadTask? = null

    constructor(context: Context?) {
        this.context = context
        //Inicialización de Firebase
        FirebaseAPP(context)
        this.storage = FirebaseStorage.getInstance()
        storageref = storage?.reference
    }

    fun insertarImagenesenFirebaseStorage(url: String, imagen: ByteArray) {
        var metadata = storageMetadata {
            contentType = "image/jpg"
        }
        imageRef = storageref?.child(url)
        uploadTask = imageRef?.putBytes(imagen, metadata)
        uploadTask?.addOnFailureListener {
            it.printStackTrace()
        }?.addOnSuccessListener { taskSnapshot ->

        }
        uploadTask?.addOnProgressListener { (bytesTransferred, totalByteCount) ->
            val progress = (100.0 * bytesTransferred) / totalByteCount

            Log.d(TAG, "Upload is $progress% done")
        }?.addOnPausedListener {
            Log.d(TAG, "Upload is paused")
        }
    }


}