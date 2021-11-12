package com.ikhwankoto.sampleworkmanagerretrofit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.workDataOf
import com.ikhwankoto.sampleworkmanagerretrofit.workers.NewsWorker
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class NewsWorkerTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    var wmRule = WorkManagerTestRule()

    @Test
    fun testSuccess() {
        // Input data
//        val inputDataUri = copyFileFromTestToTargetCtx(
//            wmRule.testContext,
//            wmRule.targetContext,
//            "test_image.png")
//        val inputData = workDataOf(KEY_IMAGE_URI to inputDataUri.toString())

        // Create request
        val request = OneTimeWorkRequestBuilder<NewsWorker>()
//            .setInputData(inputData)
            .build()

        // Enqueue and wait for result. This also runs the Worker synchronously
        // because we are using a SynchronousExecutor.
        wmRule.workManager.enqueue(request).result.get()

        // Get WorkInfo
        val workInfo = wmRule.workManager.getWorkInfoById(request.id).get()
        val outputData = workInfo.outputData.getString(NewsWorker.tagResult)

        // Assert
        Assert.assertThat(outputData, CoreMatchers.`is`("Success Message from News Worker"))
        Assert.assertThat(workInfo.state, CoreMatchers.`is`(WorkInfo.State.SUCCEEDED))
    }

}