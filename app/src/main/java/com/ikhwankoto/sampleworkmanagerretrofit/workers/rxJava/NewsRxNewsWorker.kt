package com.ikhwankoto.sampleworkmanagerretrofit.workers.rxJava

import android.content.Context
import android.util.Log
import androidx.work.*
import com.google.gson.Gson
import com.ikhwankoto.sampleworkmanagerretrofit.Preference
import com.ikhwankoto.sampleworkmanagerretrofit.api.ApiClient
import com.ikhwankoto.sampleworkmanagerretrofit.api.HewanDao
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.reactivestreams.Subscriber
import retrofit2.Call
import java.util.function.Consumer

class NewsRxNewsWorker(appContext: Context, workerParams: WorkerParameters) :
    RxWorker(appContext, workerParams) {

    companion object {
        val tagResult = "TagNewsWorkerResult"
        val tagResultData = "TagNewsWorkerResultData"
        val tag = "TAG_NEWS_RX_WORKER"
    }

    override fun createWork(): Single<Result> {
        Log.e("Ikhwan wm", "createWork")
        setProgressAsync(workDataOf("progress" to 0))

        return ApiClient.createWebService().getListDataHewan4()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : SingleObserver<HewanDao> {
//                override fun onSuccess(t: HewanDao) {
//                }
//                override fun onSubscribe(d: Disposable) {
//                }
//                override fun onError(e: Throwable) {
//                }
//            })
//            .doOnSuccess {
//                it?.let {
//                    Log.e("Ikhwan wm", "doOnSuccess " + Gson().toJson(it))
//                } ?: run {
//                    Log.e("Ikhwan wm", "doOnSuccess null")
//                }
//            }
            .map {
                Log.e("Ikhwan wm", "map")
                setProgressAsync(workDataOf("progress" to 100))
                Result.success()
            }
//            .onErrorReturn {
//                Log.e("Ikhwan wm", "onErrorReturn " + it.localizedMessage?.toString())
//                Result.failure()
//            }
    }

}