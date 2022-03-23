package com.os.daviscupindia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.os.daviscupindia.auth.WelcomeActivity
import com.os.daviscupindia.ui.home.MainActivity
import com.os.daviscupindia.utils.Constants
import com.os.daviscupindia.utils.MyApplication

class SplashScreen : AppCompatActivity() {
    // Splash screen timer
    private val SPLASH_TIME_OUT = 3000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        Handler().postDelayed(
            {
                val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                    finish()

//                if (!MyApplication.tinyDB.getBoolean(Constants.LOGGGED_IN, false)) {
//                    val i = Intent(this, WelcomeActivity::class.java)
//                    startActivity(i)
//                    finish()
//                } else {
//                    val i = Intent(this, MainActivity::class.java)
//                    startActivity(i)
//                    finish()
//                }
            }, SPLASH_TIME_OUT
        )
    }
}
