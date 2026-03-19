package com.example.sos

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.TextStyle

import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback

class SosWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            SosWidgetContent()
        }
    }

    @Composable
    private fun SosWidgetContent() {
        Column(
            modifier = GlanceModifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SafeWalk SOS",
                style = TextStyle(fontWeight = androidx.glance.text.FontWeight.Bold)
            )
            Spacer(modifier = GlanceModifier.height(12.dp))

            // EL BOTÓN ROJO DE TU DISEÑO
            Button(
                text = "PEDIR AYUDA",
                onClick = actionRunCallback<SosActionCallback>()
            )
        }
    }
}

// Esta clase conecta el botón del widget con el SosManager que ya creamos
class SosActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: androidx.glance.action.ActionParameters

    ) {
        val sosManager = SosManager(context)
        sosManager.enviarAuxilio()
    }
}