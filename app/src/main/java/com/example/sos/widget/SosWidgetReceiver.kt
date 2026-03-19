package com.example.sos.widget

import androidx.glance.appwidget.GlanceAppWidgetReceiver

class SosWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: SosWidget = SosWidget()
}