package com.ikhwankoto.sampleworkmanagerretrofit.workers.custom

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.ikhwankoto.sampleworkmanagerretrofit.api.ApiInterface
import com.ikhwankoto.sampleworkmanagerretrofit.api.HewanDao

//  this worker will create runtime error because another param added to this worker,
//  follow this step:
//  1. Disable the default initialization
//  2. Implement a custom WorkerFactory
//  3. Create a custom configuration
//  4. Initialize WorkManager

class NewsCustomWorker(
    appContext: Context, workerParams: WorkerParameters,
    private val apiInterface: ApiInterface
) :
    CoroutineWorker(appContext, workerParams) {

    companion object {
        val tagResult = "TagNewsCustomWorkerResult"
        val tagResultData = "TagNewsCustomWorkerResultData"
        val tag = "TAG_NEWS_CORO_Custom_WORKER"
    }

    //By default, this is [Dispatchers.Default]
    override suspend fun doWork(): Result {
        Log.e("CheckLog", "$tag: do news work run")
        val result: HewanDao? = apiInterface.getListDataHewan3()

        result?.let {
            return ListenableWorker.Result.success(
                Data.Builder().putString(tagResult, "Success Message from News Worker").putString(
                    tagResultData, Gson().toJson(result)
                ).build()
            )
        }
        return ListenableWorker.Result.failure()
    }

}