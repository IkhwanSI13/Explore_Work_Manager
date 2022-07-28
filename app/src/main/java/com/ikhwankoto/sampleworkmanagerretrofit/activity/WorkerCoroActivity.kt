package com.ikhwankoto.sampleworkmanagerretrofit.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.ikhwankoto.sampleworkmanagerretrofit.R
import com.ikhwankoto.sampleworkmanagerretrofit.workers.coroutines.NewsCoro2Worker
import com.ikhwankoto.sampleworkmanagerretrofit.workers.coroutines.NewsCoroWorker
import com.ikhwankoto.sampleworkmanagerretrofit.workers.custom.NewsCustomWorker
import com.ikhwankoto.sampleworkmanagerretrofit.workers.rxJava.NewsRxNewsWorker
import kotlinx.android.synthetic.main.activity_worker_coro.*

class WorkerCoroActivity : AppCompatActivity() {

    private lateinit var mWorkManager: WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_coro)

        mWorkManager = WorkManager.getInstance(this)

        workListener()
        clickListener()
    }

    private fun workListener() {
        WorkManager.getInstance(this).getWorkInfosForUniqueWorkLiveData(NewsCoroWorker.tag)
            .observe(this, Observer { workInfo ->
                var cond = "Status Work Manager\n" + NewsCoroWorker.tag
                if (workInfo != null) {
                    for ((index, data) in workInfo.withIndex()) {
                        Log.e("checkLog", "progress: ${data.progress.getInt("progress", 0)}")
                        cond += when (data.state) {
                            WorkInfo.State.RUNNING -> {
                                "\n$index - RUNNING\n${
                                    data.progress.getInt("progress", 0)
                                }"
                            }
                            WorkInfo.State.ENQUEUED -> "\n$index -  ENQUEUED"
                            WorkInfo.State.FAILED -> "\n$index - FAILED"
                            WorkInfo.State.BLOCKED -> "\n$index - BLOCKED"
                            WorkInfo.State.CANCELLED -> "\n$index - CANCELLED"
                            WorkInfo.State.SUCCEEDED -> {
                                "\n$index - SUCCEEDED\n${
                                    (data.outputData.getString(
                                        NewsCoroWorker.tagResultData
                                    )) ?: "-"
                                }"
                            }
                        }
                    }
                }
                tv_work_status.text = cond
            })

        WorkManager.getInstance(this).getWorkInfosForUniqueWorkLiveData(NewsCustomWorker.tag)
            .observe(this, Observer { workInfo ->
                var cond = "Status Work Manager\n" + NewsCoroWorker.tag
                if (workInfo != null) {
                    for ((index, data) in workInfo.withIndex()) {
                        cond += when (data.state) {
                            WorkInfo.State.RUNNING -> "\n$index - RUNNING"
                            WorkInfo.State.ENQUEUED -> "\n$index -  ENQUEUED"
                            WorkInfo.State.FAILED -> "\n$index - FAILED"
                            WorkInfo.State.BLOCKED -> "\n$index - BLOCKED"
                            WorkInfo.State.CANCELLED -> "\n$index - CANCELLED"
                            WorkInfo.State.SUCCEEDED -> {
                                "\n$index - SUCCEEDED\n${
                                    (data.outputData.getString(
                                        NewsCoroWorker.tagResultData
                                    )) ?: "-"
                                }"
                            }
                        }
                    }
                }
                tv_work_status.text = cond
            })
    }

    private fun clickListener() {
        btn_coro_work.setOnClickListener {
            mWorkManager.beginUniqueWork(
                NewsCoroWorker.tag,
                ExistingWorkPolicy.REPLACE, OneTimeWorkRequest.from(NewsCoroWorker::class.java)
            ).enqueue()
        }

        btn_coro_io_work.setOnClickListener {
            mWorkManager.beginUniqueWork(
                NewsCoro2Worker.tag,
                ExistingWorkPolicy.REPLACE, OneTimeWorkRequest.from(NewsCoro2Worker::class.java)
            ).enqueue()
        }

        btn_coro_costom_work.setOnClickListener {
            mWorkManager.beginUniqueWork(
                NewsCustomWorker.tag,
                ExistingWorkPolicy.REPLACE, OneTimeWorkRequest.from(NewsCustomWorker::class.java)
            ).enqueue()
        }
    }


}