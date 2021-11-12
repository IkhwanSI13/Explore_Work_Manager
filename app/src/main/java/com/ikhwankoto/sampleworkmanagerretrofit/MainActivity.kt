package com.ikhwankoto.sampleworkmanagerretrofit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ikhwankoto.sampleworkmanagerretrofit.activity.WorkerActivity
import com.ikhwankoto.sampleworkmanagerretrofit.activity.WorkerCoroActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_normal.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    WorkerActivity::class.java
                )
            )
        }
        btn_coroutines.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    WorkerCoroActivity::class.java
                )
            )
        }
    }

}