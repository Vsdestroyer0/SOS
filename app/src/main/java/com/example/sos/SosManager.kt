package com.example.sos

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.SmsManager
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority // IMPORTANTE: Asegúrate de usar este

class SosManager(private val context: Context) {
    private val repository = SafeWalkRepository(context)
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun enviarAuxilio() {
        val numero = repository.obtenerNumero()
        val mensajeBase = repository.obtenerMensaje()

        if (numero.isEmpty()) return

        // Usamos la prioridad de Google Play Services, no la de RenderScript
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                val linkMaps = if (location != null) {
                    "\nMi ubicación: https://www.google.com/maps?q=${location.latitude},${location.longitude}"
                } else {
                    "\n(Ubicación no disponible)"
                }

                enviarSms(numero, mensajeBase + linkMaps)
            }
    }

    private fun enviarSms(numero: String, texto: String) {
        try {
            val smsManager: SmsManager = context.getSystemService(SmsManager::class.java)
                ?: SmsManager.getDefault() // Soporte para versiones antiguas o capas personalizadas

            // Dividir el mensaje si es muy largo (evita que falle si el texto supera 160 chars)
            val parts = smsManager.divideMessage(texto)
            smsManager.sendMultipartTextMessage(numero, null, parts, null, null)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}