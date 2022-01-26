/*
 * Copyright (c) 2021. Project made by Jesús Rodríguez Malagón
 */

package com.jesusrodriguez.ejerciciofinal

import android.net.Uri
import org.json.JSONObject

import org.json.JSONException

import org.json.JSONArray
import java.io.*
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL


class JSONParser()
{
    /**
     * Conecta con el servidor y devuelve un JSONArray con los datos obtenidos
     * @param direccionurl URL del servidor
     * @param parametros Parámetros de la consulta
     * @return JSONArray con los resultados de la consulta al servidor
     */
    @Throws(JSONException::class, IOException::class)
    fun getJSONArrayFromUrl(direccionurl: String, parametros: HashMap<String, String>?): JSONArray {
        val url: URL
        val response = StringBuilder()
        url = URL(buildURL(direccionurl, parametros))
        val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
        urlConnection.setReadTimeout(15000)
        urlConnection.setConnectTimeout(15000)
        urlConnection.setRequestMethod("GET")
        urlConnection.setRequestProperty("Content-type", "application/json")
        val responseCode: Int = urlConnection.getResponseCode()
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val `in`: InputStream = BufferedInputStream(urlConnection.getInputStream())
            val reader = BufferedReader(InputStreamReader(`in`))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
        }
        urlConnection.disconnect()
        return JSONArray(response.toString())
    }

    /**
     * Conecta con el servidor y devuelve un JSONObject con los datos obtenidos
     * @param direccionurl URL del servidor
     * @param parametros Parámetros de la consulta
     * @return JSONArray con los resultados de la consulta al servidor
     */
    @Throws(JSONException::class, IOException::class)
   fun getJSONObjectFromUrl(
        direccionurl: String,
        parametros: HashMap<String, String>?
    ): JSONObject {
        val url: URL
        val response = StringBuilder()
        url = URL(buildURL(direccionurl, parametros))
        val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
        urlConnection.setReadTimeout(15000)
        urlConnection.setConnectTimeout(15000)
        urlConnection.setRequestMethod("GET")
        urlConnection.setRequestProperty("Content-type", "application/json")
        val responseCode: Int = urlConnection.getResponseCode()
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val `in`: InputStream = BufferedInputStream(urlConnection.getInputStream())
            val reader = BufferedReader(InputStreamReader(`in`))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
        }
        urlConnection.disconnect()
        return JSONObject(response.toString())
    }

    /**
     * Crea una URL válida con parámetros
     * @param url URL base
     * @param params Parámetros para la URL
     * @return URL formateada con sus parámetros
     */
    private fun buildURL(url: String, params: HashMap<String, String>?): String {
        val builder: Uri.Builder = Uri.parse(url).buildUpon()
        if (params != null) {
            for ((key, value) in params) {
                builder.appendQueryParameter(key, value)
            }
        }
        return builder.build().toString()
    }

    companion object {
        const val TAG = "JSONParser"
    }
}