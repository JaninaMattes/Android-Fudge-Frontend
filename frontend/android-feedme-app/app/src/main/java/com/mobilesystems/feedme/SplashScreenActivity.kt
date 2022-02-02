package com.mobilesystems.feedme

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

/**
 * The purpose of a Splash Screen is to display a screen while the application
 * fetches the relevant content if any (from network calls/database).
 *
 * https://medium.com/geekculture/implementing-the-perfect-splash-screen-in-android-295de045a8dc
 */

class SplashScreenActivity : AppCompatActivity() {

    val activityScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityScope.launch {
            // show splash screen for 3 seconds
            delay(3000)

            // load all data ...

            // navigate to login
            val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    override fun onPause() {
        // Destroy splash screen, when it goes to main activity
        super.onPause()
        activityScope.cancel()
    }
}