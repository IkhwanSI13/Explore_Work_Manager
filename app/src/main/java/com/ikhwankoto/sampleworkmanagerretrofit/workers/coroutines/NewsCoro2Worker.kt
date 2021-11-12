package com.ikhwankoto.sampleworkmanagerretrofit.workers.coroutines

import android.content.Context
import android.util.Log
import androidx.work.*
import com.google.gson.Gson
import com.ikhwankoto.sampleworkmanagerretrofit.Preference
import com.ikhwankoto.sampleworkmanagerretrofit.api.ApiClient
import com.ikhwankoto.sampleworkmanagerretrofit.api.HewanDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsCoro2Worker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    companion object {
        val tagResult = "TagNewsWorkerResult"
        val tagResultData = "TagNewsWorkerResultData"
        val tag = "TAG_NEWS_CORO2_WORKER"
    }

    //By default, this is [Dispatchers.Default]
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            setProgress(workDataOf("progress" to 0))
            Preference().setCountValue(
                applicationContext,
                (Preference().getCountValue(applicationContext, Preference.tagCountWork) + 1),
                Preference.tagCountWork
            )

            setProgress(workDataOf("progress" to 50))
            val result: HewanDao? = ApiClient.createWebService().getListDataHewan3()
            setProgress(workDataOf("progress" to 100))
            result?.let {
                Log.e("CheckLog","trace run")
                ListenableWorker.Result.success(
                    Data.Builder().putString(tagResult, "Success Message from News Worker")
                        .putString(
                            tagResultData, Gson().toJson(result)
                        ).build()
                )
            } ?: run {
                Log.e("CheckLog","trace run else")
                ListenableWorker.Result.failure()
            }
        }
    }

}