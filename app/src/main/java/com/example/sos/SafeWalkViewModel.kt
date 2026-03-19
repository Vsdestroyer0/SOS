package com.example.sos

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SafeWalkUiState(
    val numero: String = "",
    val mensaje: String = "",
    val rastreoActivo: Boolean = false
)

class SafeWalkViewModel(private val repository: SafeWalkRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(SafeWalkUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Carga inicial de datos desde la memoria
        _uiState.update { it.copy(
            numero = repository.obtenerNumero(),
            mensaje = repository.obtenerMensaje(),
            rastreoActivo = repository.obtenerRastreo()
        )}
    }

    fun actualizarFormulario(num: String, msj: String, activo: Boolean) {
        _uiState.update { it.copy(numero = num, mensaje = msj, rastreoActivo = activo) }
    }

    fun guardarConfiguracion() {
        repository.guardarDatos(
            uiState.value.numero,
            uiState.value.mensaje,
            uiState.value.rastreoActivo
        )
    }
}