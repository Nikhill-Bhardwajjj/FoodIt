package com.lihkin16.foodit_3

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            val intent = Intent(
                this@SplashActivity,
                StartActivity::class.java
            )
            startActivity(intent)
            finish()
        }, 1200) // 2000 milliseconds (2 seconds) delay


    }
}