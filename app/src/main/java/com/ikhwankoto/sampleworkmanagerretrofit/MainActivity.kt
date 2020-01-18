package com.ikhwankoto.sampleworkmanagerretrofit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.ikhwankoto.sampleworkmanagerretrofit.workers.NewsSecondWorker
import com.ikhwankoto.sampleworkmanagerretrofit.workers.NewsWorker
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var mWorkManager: WorkManager
//    private lateinit var mSavedWorkInfo: LiveData<List<WorkInfo>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_one_work.setOnClickListener {
            Log.e("CheckLog", "btn_one_work clicked")
            mWorkManager = WorkManager.getInstance(this)
            mWorkManager.beginUniqueWork(
                NewsWorker.CONST_OUTPUT,
                ExistingWorkPolicy.REPLACE, OneTimeWorkRequest.from(NewsWorker::class.java)
            ).enqueue()
        }

        btn_more_work.setOnClickListener {
            Log.e("CheckLog", "btn_more_work clicked")
            mWorkManager = WorkManager.getInstance(this)
            var workCon = mWorkManager.beginUniqueWork(
                NewsWorker.CONST_OUTPUT,
                ExistingWorkPolicy.REPLACE, OneTimeWorkRequest.from(NewsWorker::class.java)
            )

            workCon = workCon.then(OneTimeWorkRequest.from(NewsSecondWorker::class.java))
            workCon.enqueue()
        }

        btn_work_network.setOnClickListener {
            Log.e("CheckLog", "btn_work_network clicked")
            mWorkManager = WorkManager.getInstance(this)
            mWorkManager.beginUniqueWork(
                NewsWorker.CONST_OUTPUT,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.Builder(NewsWorker::class.java)
                    .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                    .build()
            ).enqueue()
        }

        btn_periodic_work.setOnClickListener {
            Log.e("CheckLog", "btn_periodic_work clicked")
            mWorkManager = WorkManager.getInstance(this)
            //15 minute minimal, below of 15, make work manager can't run correctly
            val periodicWork =
                PeriodicWorkRequest.Builder(NewsWorker::class.java, 16, TimeUnit.MINUTES).build()
            mWorkManager.enqueue(periodicWork)
        }

        /// Available on work manager with 2.1.0 version
        btn_periodic_work_initial_delay.setOnClickListener {
            Log.e("CheckLog", "btn_periodic_work_initial_delay clicked")
            mWorkManager = WorkManager.getInstance(this)
            //15 minute minimal, below of 15, make work manager can't run correctly
            val periodicWork =
                PeriodicWorkRequest.Builder(
                    NewsWorker::class.java,
                    16,
                    TimeUnit.MINUTES
                ).setInitialDelay(15, TimeUnit.MINUTES).build()
            mWorkManager.enqueue(periodicWork)
        }
    }

}