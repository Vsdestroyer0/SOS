package com.example.sos

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ConfigScreen(viewModel: SafeWalkViewModel) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // ESTO ES LO NUEVO: El "lanzador" de la ventanita de permisos
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val gpsOk = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val smsOk = permissions[Manifest.permission.SEND_SMS] ?: false

        if (gpsOk && smsOk) {
            viewModel.guardarConfiguracion()
            Toast.makeText(context, "Configuración guardada y permisos listos", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Faltan permisos críticos", Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Configuración",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Estado del servicio: ${if(state.rastreoActivo) "Activo" else "Inactivo"}",
            color = if(state.rastreoActivo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Sección: Contacto de Emergencia
        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Contacto de Emergencia", fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = state.numero,
                    onValueChange = { viewModel.actualizarFormulario(it, state.mensaje, state.rastreoActivo) },
                    label = { Text("Número de contacto") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        // Sección: Mensaje de Auxilio
        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Mensaje de Auxilio", fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = state.mensaje,
                    onValueChange = { viewModel.actualizarFormulario(state.numero, it, state.rastreoActivo) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        }

        // Sección: Rastreo cada 5 min
        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Rastreo cada 5 min", modifier = Modifier.weight(1f))
                Switch(
                    checked = state.rastreoActivo,
                    onCheckedChange = { viewModel.actualizarFormulario(state.numero, state.mensaje, it) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // BOTÓN ACTUALIZADO
        Button(
            onClick = {
                // Al picarle, lanza la solicitud de permisos
                launcher.launch(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.SEND_SMS
                ))
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Text("Guardar y Vincular Widget")
        }
    }
}