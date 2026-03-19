package com.example.sos

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.work.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.TimeUnit

data class SafeWalkUiState(
    val numero: String = "",
    val mensaje: String = "",
    val rastreoActivo: Boolean = false
)

class SafeWalkViewModel(private val repository: SafeWalkRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(SafeWalkUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(
            numero = repository.obtenerNumero(),
            mensaje = repository.obtenerMensaje(),
            rastreoActivo = repository.obtenerRastreo()
        )}
    }

    fun actualizarFormulario(num: String, msj: String, activo: Boolean) {
        _uiState.update { it.copy(numero = num, mensaje = msj, rastreoActivo = activo) }
    }

    fun guardarConfiguracion(context: Context) {
        // 1. Guardamos en SharedPreferences
        repository.guardarDatos(
            uiState.value.numero,
            uiState.value.mensaje,
            uiState.value.rastreoActivo
        )

        // 2. Sincronizamos el estado del rastreo con WorkManager
        gestionarRastreoBackground(context, uiState.value.rastreoActivo)
    }

    private fun gestionarRastreoBackground(context: Context, activo: Boolean) {
        val workManager = WorkManager.getInstance(context)

        if (activo) {
            // Android restringe el intervalo mínimo a 15 minutos por ahorro de batería.
            val rastreoRequest = PeriodicWorkRequestBuilder<SosWorker>(15, TimeUnit.MINUTES)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()

            workManager.enqueueUniquePeriodicWork(
                "RastreoSeguro",
                ExistingPeriodicWorkPolicy.UPDATE, // Si ya existe, actualiza la configuración
                rastreoRequest
            )
        } else {
            // Si el usuario apaga el switch, cancelamos cualquier tarea pendiente
            workManager.cancelUniqueWork("RastreoSeguro")
        }
    }
}