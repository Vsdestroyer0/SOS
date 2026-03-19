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
                    // Formato correcto para que el receptor pueda abrir el mapa con un clic
                    "\nMi ubicación: https://www.google.com/maps?q=${location.latitude},${location.longitude}"
                } else {
                    "\n(Ubicación no disponible)"
                }

                enviarSms(numero, mensajeBase + linkMaps)
            }
    }

    private fun enviarSms(numero: String, texto: String) {
        try {
            val smsManager = context.getSystemService(SmsManager::class.java)
            smsManager.sendTextMessage(numero, null, texto, null, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}