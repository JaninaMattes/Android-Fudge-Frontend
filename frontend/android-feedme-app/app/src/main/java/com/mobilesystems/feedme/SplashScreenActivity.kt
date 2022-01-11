package com.mobilesystems.feedme

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * The purpose of a Splash Screen is to display a screen while the application
 * fetches the relevant content if any (from network calls/database).
 *
 * https://medium.com/geekculture/implementing-the-perfect-splash-screen-in-android-295de045a8dc
 */

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val timer: Thread = object : Thread() {
            override fun run() {
                try {
                    //Display for 3 seconds
                    sleep(3000)
                } catch (e: InterruptedException) {
                    // TODO: handle exception
                    e.printStackTrace()
                } finally {
                    val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        timer.start()
    }

    override fun onPause() {
        // Destroy splash screen, when it goes to main activity
        super.onPause()
        finish()
    }
}