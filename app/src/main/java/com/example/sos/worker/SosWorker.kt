package com.example.sos.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.sos.data.SafeWalkRepository
import com.example.sos.data.SosManager

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