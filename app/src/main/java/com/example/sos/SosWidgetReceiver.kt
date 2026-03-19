package com.example.sos

import androidx.glance.appwidget.GlanceAppWidgetReceiver

class SosWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: SosWidget = SosWidget()
}