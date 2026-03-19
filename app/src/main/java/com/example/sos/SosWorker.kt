package com.example.sos

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class SosWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        val repository = SafeWalkRepository(applicationContext)

        // Solo envía si el usuario dejó el switch activado
        if (repository.obtenerRastreo()) {
            val sosManager = SosManager(applicationContext)
            sosManager.enviarAuxilio()
            return Result.success()
        }
        return Result.retry()
    }
}