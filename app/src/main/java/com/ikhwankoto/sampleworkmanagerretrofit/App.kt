package com.ikhwankoto.sampleworkmanagerretrofit

import android.app.Application
import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import com.ikhwankoto.sampleworkmanagerretrofit.api.ApiClient
import com.ikhwankoto.sampleworkmanagerretrofit.workers.custom.MyWorkerFactory

class App : Application(), Configuration.Provider {

    //  Manual Work Manager Init
    override fun getWorkManagerConfiguration(): Configuration {
        val myWorkerFactory = DelegatingWorkerFactory()
        myWorkerFactory.addFactory(MyWorkerFactory(ApiClient.createWebService()))

        return if (BuildConfig.DEBUG) {
            Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.DEBUG)
                // if we are using WorkManager as well as the JobScheduler API in our application.
                // In this case you want to avoid using the same JobId range in both places.
                //.setJobSchedulerJobIdRange()

                // Option 1 for custom worker factory (single)
                //.setWorkerFactory(MyWorkerFactory(ApiClient.createWebService()))

                // Option 2 for custom worker factory (multiple)
                .setWorkerFactory(myWorkerFactory)
                .build()
        } else {
            Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.ERROR)
                .build()
        }
    }

}