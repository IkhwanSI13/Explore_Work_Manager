package com.ikhwankoto.sampleworkmanagerretrofit.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.*
import com.google.gson.Gson
import com.ikhwankoto.sampleworkmanagerretrofit.Preference
import com.ikhwankoto.sampleworkmanagerretrofit.R
import com.ikhwankoto.sampleworkmanagerretrofit.workers.NewsPeriodicWorker
import com.ikhwankoto.sampleworkmanagerretrofit.workers.NewsSecondWorker
import com.ikhwankoto.sampleworkmanagerretrofit.workers.NewsWorker
import kotlinx.android.synthetic.main.activity_worker.*
import java.util.concurrent.TimeUnit

/**
 * Note for Behavior:
 *
 * https://stackoverflow.com/questions/50682061/android-is-workmanager-running-when-app-is-closed
 *
 * https://stackoverflow.com/questions/55349488/work-manager-not-working-when-app-is-killed-by-the-user-why
 * As I found out, the work manager depends on the device manufacturer.
 * For my case, it is an miui device, which does not allow work manager to work in case
 * the app is killed or rebooted. The work manager worked when I provided the
 * application with "autostart permission".
 * */

class WorkerActivity : AppCompatActivity() {

    var PERIODIC_WORKER = "TAG_PERIODIC_WORKER"

    var PERIODIC_WORKER_UNIQUE1 = "PERIODIC_WORKER_1"
    var PERIODIC_WORKER_UNIQUE2 = "PERIODIC_WORKER_2"

    private lateinit var mWorkManager: WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker)
        mWorkManager = WorkManager.getInstance(this)

        setCount()
        setPeriodicCount()
        workListener()
        onClickBtn()
    }

    private fun setCount() {
        val count = Preference().getCountValue(this, Preference.tagCountWork)
        val countSecondWork = Preference().getCountValue(this, Preference.tagCountSecondWork)
        tv_counter.text = "Count Work: $count\nCount Second Work: $countSecondWork"
    }

    private fun setPeriodicCount() {
        val count = Preference().getCountValue(this, Preference.tagCountPeriodicWork)
        tv_count_periodic.text = "Count Periodic Work: $count"
    }

    private fun workListener() {
        //  The progress data is only available while the Worker is in RUNNING state
        WorkManager.getInstance(this).getWorkInfosForUniqueWorkLiveData(NewsWorker.tag)
            .observe(this, Observer { workInfo ->
                var cond = "Status " + NewsWorker.tag
                Log.e("CheckLog", "Gson() work info: " + Gson().toJson(workInfo))
                if (workInfo != null) {
                    for ((index, data) in workInfo.withIndex()) {
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
                                "\n$index - SUCCEEDED\n${data.outputData.getString(NewsWorker.tagResult)}"
                            }
                        }
                        btn_work_cancel.isEnabled =
                            (data.state == WorkInfo.State.RUNNING || data.state == WorkInfo.State.ENQUEUED)
                    }
                } else {
                    tv_work_status.text = "null"
                    btn_work_cancel.isEnabled = false
                }
                tv_work_status.text = cond
            })

        WorkManager.getInstance(this).getWorkInfosByTagLiveData(PERIODIC_WORKER)
            .observe(this, Observer { workInfo ->
                var cond = "Status " + PERIODIC_WORKER
                if (workInfo != null)
                    for ((index, data) in workInfo.withIndex())
                        cond += when (data.state) {
                            WorkInfo.State.RUNNING -> "\n$index - RUNNING"
                            WorkInfo.State.ENQUEUED -> "\n$index -  ENQUEUED"
                            WorkInfo.State.FAILED -> "\n$index - FAILED"
                            WorkInfo.State.BLOCKED -> "\n$index - BLOCKED"
                            WorkInfo.State.CANCELLED -> "\n$index - CANCELLED"
                            WorkInfo.State.SUCCEEDED -> "\n$index - SUCCEEDED"
                        }
                else {
                    tv_work_status_periodic.text = "null"
                }

                tv_work_status_periodic.text = cond
            })
    }

    private fun onClickBtn() {
        btn_one_work.setOnClickListener {
            Log.e("CheckLog", "btn_one_work clicked")
            setCount()
            mWorkManager.beginUniqueWork(
                NewsWorker.tag,
                ExistingWorkPolicy.REPLACE, OneTimeWorkRequest.from(NewsWorker::class.java)
            ).enqueue()
        }

        btn_one_work_send_data.setOnClickListener {
            Log.e("CheckLog", "btn_one_work_send_data clicked")
            setCount()
            val worker = OneTimeWorkRequestBuilder<NewsWorker>()
                .setInputData(createInputDataForWorker())
                .build()

            mWorkManager.beginUniqueWork(
                NewsWorker.tag, ExistingWorkPolicy.REPLACE, worker
            ).enqueue()
        }

        btn_more_work.setOnClickListener {
            Log.e("CheckLog", "btn_more_work clicked")
            setCount()
            var workCon = mWorkManager.beginUniqueWork(
                NewsWorker.tag,
                ExistingWorkPolicy.REPLACE, OneTimeWorkRequest.from(NewsWorker::class.java)
            )

            workCon = workCon.then(OneTimeWorkRequest.from(NewsSecondWorker::class.java))
            workCon.enqueue()
        }

        btn_work_network.setOnClickListener {
            Log.e("CheckLog", "btn_work_network clicked")
            setCount()

            val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
//                            .setRequiresCharging(true)
//                            .setRequiresBatteryNotLow(true)
//                            .setRequiresDeviceIdle(true)
//                            .setRequiresStorageNotLow(true)
                .build()

            mWorkManager.beginUniqueWork(
                NewsWorker.tag,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.Builder(NewsWorker::class.java)
                    .setConstraints(constraints)
                    .build()
            ).enqueue()
        }

        //Periodic
        btn_work_cancel.setOnClickListener {
            Log.e("CheckLog", "btn_work_cancel clicked")
            mWorkManager.cancelUniqueWork(NewsWorker.tag)
        }

        btn_periodic_work.setOnClickListener {
            Log.e("CheckLog", "btn_periodic_work clicked")
            setCount()
            //15 minute minimal, below of 15, make work manager can't run correctly
            val periodicWork =
                PeriodicWorkRequest.Builder(NewsPeriodicWorker::class.java, 16, TimeUnit.MINUTES)
                    .addTag(PERIODIC_WORKER).build()
            mWorkManager.enqueueUniquePeriodicWork(
                PERIODIC_WORKER_UNIQUE1,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWork
            )
        }

        /// Available on work manager with 2.1.0 version
        btn_periodic_work_initial_delay.setOnClickListener {
            Log.e("CheckLog", "btn_periodic_work_initial_delay clicked")
            setCount()
            //15 minute minimal, below of 15, make work manager can't run correctly
            val periodicWork =
                PeriodicWorkRequest.Builder(
                    NewsPeriodicWorker::class.java,
                    16,
                    TimeUnit.MINUTES
                ).setInitialDelay(15, TimeUnit.MINUTES).build()
            mWorkManager.enqueueUniquePeriodicWork(
                PERIODIC_WORKER_UNIQUE2,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWork
            )
        }
    }

    private fun createInputDataForWorker(): Data {
        val builder = Data.Builder()
        builder.putString("KEY_DATA", "12345")
        return builder.build()
    }

}