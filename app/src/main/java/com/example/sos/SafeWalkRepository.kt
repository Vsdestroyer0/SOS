package com.example.sos

import android.content.Context
import android.content.SharedPreferences

class SafeWalkRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("SafeWalkPrefs", Context.MODE_PRIVATE)

    fun guardarDatos(numero: String, mensaje: String, rastreoActivo: Boolean) {
        prefs.edit().apply {
            putString("numero_contacto", numero)
            putString("mensaje_auxilio", mensaje)
            putBoolean("rastreo_activo", rastreoActivo)
            apply()
        }
    }

    fun obtenerNumero() = prefs.getString("numero_contacto", "") ?: ""
    fun obtenerMensaje() = prefs.getString("mensaje_auxilio", "¡Necesito ayuda! Mi ubicación actual es: ") ?: ""
    fun obtenerRastreo() = prefs.getBoolean("rastreo_activo", false)
}