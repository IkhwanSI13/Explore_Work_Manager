package com.ikhwankoto.sampleworkmanagerretrofit.workers.coroutines

import android.content.Context
import android.util.Log
import androidx.work.*
import com.google.gson.Gson
import com.ikhwankoto.sampleworkmanagerretrofit.Preference
import com.ikhwankoto.sampleworkmanagerretrofit.api.ApiClient
import com.ikhwankoto.sampleworkmanagerretrofit.api.HewanDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

class NewsCoroWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    companion object {
        val tagResult = "TagNewsWorkerResult"
        val tagResultData = "TagNewsWorkerResultData"
        val tag = "TAG_NEWS_CORO_WORKER"
    }

    //By default, this is [Dispatchers.Default]
    override suspend fun doWork(): Result {
        Log.e("CheckLog", "$tag: do news work run")
        val inputData: String? = inputData.getString("KEY_DATA")
        inputData?.let {
            Log.e("CheckLog", "$tag: InputData: $inputData")
        }

        setProgress(workDataOf("progress" to 0))
        Preference().setCountValue(
            applicationContext,
            (Preference().getCountValue(applicationContext, Preference.tagCountWork) + 1),
            Preference.tagCountWork
        )

        ///Sample
        withContext(Dispatchers.IO) { }
        supervisorScope { }

        // setProgressAsync(Data.Builder().putInt("progress",10).build())
        setProgress(workDataOf("progress" to 50))

        val result: HewanDao? = ApiClient.createWebService().getListDataHewan3()
        Log.e("CheckLog", "$tag: hit: " + Gson().toJson(result))

        setProgress(workDataOf("progress" to 100))
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