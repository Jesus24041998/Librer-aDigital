/*
 * Copyright (c) 2021. Project made by Jesús Rodríguez Malagón
 */

package com.jesusrodriguez.ejerciciofinal

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * Clase DemoFragment es la encargada de crear un fragment segun la información que recibe de TabsPageAdapter y de los formularios de dicho fragmento
 */
class DemoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentName = arguments?.getString("fragmentName")
        var rootView: View? = null

        if (fragmentName.equals("Registro")) {
            rootView = inflater.inflate(R.layout.fragment_registrar, container, false)
        } else if (fragmentName.equals("Login")) {
            rootView = inflater.inflate(R.layout.fragment_logear, container, false)
        }

        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Registro
        val fragmentName = arguments?.getString("fragmentName")

        var tituloregistro: TextView
        var rtemail: TextView
        var rtcontrasena: TextView
        var reemail: EditText
        var recontrasena: EditText
        var botonregistro: Button?
        //Login
        var titulologin: TextView
        var ltemail: TextView
        var ltcontrasena: TextView
        var leemail: EditText
        var lecontrasena: EditText
        var botonlogin: Button?
        var comprobacion = AppCompatDelegate.getDefaultNightMode()
        //Registro
        if (fragmentName.equals("Registro")) {
            tituloregistro = view.findViewById(R.id.tituloRegistro)
            rtemail = view.findViewById(R.id.temailR)
            rtcontrasena = view.findViewById(R.id.tcontraR)
            reemail = view.findViewById(R.id.emailRegistro)
            recontrasena = view.findViewById(R.id.contrasenaRegistro)
            botonregistro = view.findViewById(R.id.aceptarRegistro)
            //Registro
            when (comprobacion) {
                1 -> {
                    tituloregistro.setTextColor(Color.BLACK)
                    rtemail.setTextColor(Color.BLACK)
                    rtcontrasena.setTextColor(Color.BLACK)
                    reemail.setTextColor(Color.BLACK)
                    recontrasena.setTextColor(Color.BLACK)
                    botonregistro.setTextColor(Color.BLACK)
                    true
                }
                2 -> {
                    tituloregistro.setTextColor(Color.WHITE)
                    rtemail.setTextColor(Color.WHITE)
                    rtcontrasena.setTextColor(Color.WHITE)
                    reemail.setTextColor(Color.WHITE)
                    recontrasena.setTextColor(Color.WHITE)
                    botonregistro.setTextColor(Color.WHITE)
                    true
                }
            }
            //Cuando pulsamos en registrar , si los datos no son vacios insertara un usuario a nuestra base de datos
            // siempre y cuando no exista , creara un usuario en Firebase y nos logueara e ingresara en la app automáticamente
            botonregistro.setOnClickListener {

                if (!reemail.text.toString().equals("")) {
                    if (!recontrasena.text.toString().equals("")) {
                        var auth: FirebaseAuth
                        auth = Firebase.auth

                        if (LibroSQL(context).insertarUsuario(
                                reemail.text.toString(),
                                recontrasena.text.toString()
                            )
                        ) {
                            auth.createUserWithEmailAndPassword(
                                reemail.text.toString(),
                                recontrasena.text.toString()
                            )

                            var usuario: Usuario = LibroSQL(context).seleccionarUsuario(
                                reemail.text.toString(),
                                recontrasena.text.toString()
                            )
                            auth.signInWithEmailAndPassword(
                                reemail.text.toString(),
                                recontrasena.text.toString()
                            )

                            var savePreferences: SharedPreferences =
                                requireContext().getSharedPreferences(
                                    "user",
                                    AppCompatActivity.MODE_PRIVATE
                                )
                            var objeditor: SharedPreferences.Editor = savePreferences.edit()
                            objeditor.putString("user", usuario.getIdentificador())
                            objeditor.commit()
                            var intent = Intent(context, SplashScreenActivity::class.java)

                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                context,
                                "El usuario ya existe",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    } else {
                        Toast.makeText(
                            context,
                            "No deje en blanco la contraseña",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(context, "No deje en blanco el correo", Toast.LENGTH_LONG).show()
                }

            }
        }
        //Login

        else if (fragmentName.equals("Login")) {
            titulologin = view.findViewById(R.id.tituloLogin)
            ltemail = view.findViewById(R.id.temailL)
            ltcontrasena = view.findViewById(R.id.tcontraL)
            leemail = view.findViewById(R.id.emailLogin)
            lecontrasena = view.findViewById(R.id.contrasenaLogin)
            botonlogin = view.findViewById(R.id.aceptarLogin)
            when (comprobacion) {
                1 -> {
                    titulologin.setTextColor(Color.BLACK)
                    ltemail.setTextColor(Color.BLACK)
                    ltcontrasena.setTextColor(Color.BLACK)
                    leemail.setTextColor(Color.BLACK)
                    lecontrasena.setTextColor(Color.BLACK)
                    botonlogin.setTextColor(Color.BLACK)
                    true
                }
                2 -> {
                    titulologin.setTextColor(Color.WHITE)
                    ltemail.setTextColor(Color.WHITE)
                    ltcontrasena.setTextColor(Color.WHITE)
                    leemail.setTextColor(Color.WHITE)
                    lecontrasena.setTextColor(Color.WHITE)
                    botonlogin.setTextColor(Color.WHITE)
                }
            }
            //Cuando pulsamos en iniciar sesión , si los datos no son vacios recogera un usuario desde nuestra base de datos
            // si los datos proporcionados son validos y nus ingresara en la aplicación
            botonlogin.setOnClickListener {

                if (!leemail.text.toString().equals("")) {
                    if (!lecontrasena.text.toString().equals("")) {
                        leemail.text
                        lecontrasena.text
                        var auth: FirebaseAuth
                        auth = Firebase.auth


                        var usuario: Usuario = LibroSQL(context).seleccionarUsuario(
                            leemail.text.toString(),
                            lecontrasena.text.toString()
                        )

                        if (usuario.getIdentificador() != null) {


                            var savePreferences: SharedPreferences =
                                requireContext().getSharedPreferences(
                                    "user",
                                    AppCompatActivity.MODE_PRIVATE
                                )
                            var objeditor: SharedPreferences.Editor = savePreferences.edit()
                            objeditor.putString("user", usuario.getIdentificador())
                            objeditor.commit()

                            auth.signInWithEmailAndPassword(
                                leemail.text.toString(),
                                lecontrasena.text.toString()
                            )

                            var intent = Intent(context, SplashScreenActivity::class.java)
                            onDestroy()
                            startActivity(intent)
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "No deje en blanco la contraseña",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(context, "No deje en blanco el correo", Toast.LENGTH_LONG).show()
                }
            }

        }
    }


}