package com.example.sos.UI

import android.Manifest
import android.os.Build
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

    // Launcher actualizado para manejar el flujo de permisos y guardado
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val gpsOk = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val smsOk = permissions[Manifest.permission.SEND_SMS] ?: false

        // El permiso de segundo plano es opcional en la validación inicial para que no truene,
        // pero idealmente el usuario debe aceptarlo en los ajustes.

        if (gpsOk && smsOk) {
            // Ahora pasamos el context para que WorkManager pueda iniciarse
            viewModel.guardarConfiguracion(context)
            Toast.makeText(context, "Configuración guardada y rastreo actualizado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Faltan permisos críticos (GPS o SMS)", Toast.LENGTH_LONG).show()
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
            text = "Configuración SafeWalk",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Estado: ${if(state.rastreoActivo) "Rastreo Automático Activo" else "Solo SOS Manual"}",
            color = if(state.rastreoActivo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Sección: Contacto de Emergencia
        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Contacto de Emergencia", fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = state.numero,
                    onValueChange = { viewModel.actualizarFormulario(it, state.mensaje, state.rastreoActivo) },
                    label = { Text("Número de teléfono") },
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
                Text(
                    text = "* Se añadirá la ubicación automáticamente al final.",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Sección: Rastreo (Periodicidad sujeta a restricciones de Android)
        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Rastreo Automático", fontWeight = FontWeight.Bold)
                    Text("Envía ubicación cada 15 min", style = MaterialTheme.typography.bodySmall)
                }
                Switch(
                    checked = state.rastreoActivo,
                    onCheckedChange = { viewModel.actualizarFormulario(state.numero, state.mensaje, it) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // Preparamos la lista de permisos según la versión de Android
                val permisos = mutableListOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.SEND_SMS
                )

                // Si es Android 10 o superior, el rastreo en 15 min requiere Background Location
                // Nota: Google recomienda pedirlo por separado, pero para tu tésis esto funcionará
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && state.rastreoActivo) {
                    permisos.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                }

                launcher.launch(permisos.toTypedArray())
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Guardar y Aplicar Cambios")
        }
    }
}