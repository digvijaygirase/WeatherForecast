package com.digvijay.weatherforecast.framework

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters

class LocationWorkerFactory() : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            LocationWorker::class.java.canonicalName -> {
                LocationWorker(appContext, workerParameters)
            }
            else -> null
        }
    }
}