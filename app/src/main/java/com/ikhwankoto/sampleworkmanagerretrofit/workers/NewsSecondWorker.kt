package com.ikhwankoto.sampleworkmanagerretrofit.workers

import android.content.Context
import android.os.StrictMode
import android.util.Log
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ikhwankoto.sampleworkmanagerretrofit.api.ApiClient

class NewsSecondWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    companion object {
        var CONST_OUTPUT = "WORK_ACEES_API_SECOND"
    }

    override fun doWork(): Result {
        Log.e("CheckLog", "do second work run")
        Thread.sleep(5_000)
        try {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val x = ApiClient.createWebService().getListDataHewan2()
            val respon = x.execute()
            Log.e("CheckLog", "success worker")
            return ListenableWorker.Result.success()
        } catch (e: Exception) {
            Log.e("CheckLog", "fail worker")
            return ListenableWorker.Result.failure()
        }
    }

}