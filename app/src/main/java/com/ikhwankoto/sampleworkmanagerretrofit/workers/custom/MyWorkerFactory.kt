package com.ikhwankoto.sampleworkmanagerretrofit.workers.custom

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.ikhwankoto.sampleworkmanagerretrofit.api.ApiClient
import com.ikhwankoto.sampleworkmanagerretrofit.api.ApiInterface

class MyWorkerFactory(private val apiInterface: ApiInterface) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        // Option 1
        // This only handles a single Worker, please don’t do this!!
        // See below for a better way using DelegatingWorkerFactory
        // return NewsCustomWorker(appContext, workerParameters, ApiClient.createWebService())

        // Option 2
        return when (workerClassName) {
            NewsCustomWorker::class.java.name ->
                NewsCustomWorker(appContext, workerParameters, ApiClient.createWebService())
            else ->
                // Return null, so that the base class can delegate to the default WorkerFactory.
                null
        }
    }
}