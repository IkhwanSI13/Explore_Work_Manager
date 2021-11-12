package com.ikhwankoto.sampleworkmanagerretrofit.workers

import android.content.Context
import android.os.StrictMode
import android.util.Log
import androidx.work.*
import com.ikhwankoto.sampleworkmanagerretrofit.Preference
import com.ikhwankoto.sampleworkmanagerretrofit.api.ApiClient

class NewsWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    companion object {
        val tagResult = "TagNewsWorkerResult"
        var tag = "TAG_NEWS_WORKER"
    }

    override fun doWork(): Result {
        Log.e("CheckLog $tag", "do news work run")
        Preference().setCountValue(
            applicationContext,
            (Preference().getCountValue(applicationContext, Preference.tagCountWork) + 1),
            Preference.tagCountWork
        )

        /// handle sending data to this worker
        val inputData: String? = inputData.getString("KEY_DATA")
        inputData?.let {
            Log.e("CheckLog $tag", "InputData: $inputData")
        } ?: run {
            Log.e("CheckLog $tag", "InputData: null")
        }

        /// progress
        setProgressAsync(workDataOf("progress" to 0))

        Thread.sleep(5_000)
        setProgressAsync(workDataOf("progress" to 50))

        try {
            /// hit API
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val x = ApiClient.createWebService().getListDataHewan2()
            val respon = x.execute()

            setProgressAsync(workDataOf("progress" to 100))

            /// success with output data
            val outputData =
                Data.Builder().putString(tagResult, "Success Message from News Worker").build()
            return ListenableWorker.Result.success(outputData)
        } catch (e: Exception) {
            setProgressAsync(workDataOf("progress" to 100))
            return ListenableWorker.Result.failure()
        }
    }

}