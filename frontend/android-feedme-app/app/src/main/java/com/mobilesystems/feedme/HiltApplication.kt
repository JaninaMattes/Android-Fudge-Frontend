package com.mobilesystems.feedme

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * The Hilt Application class is used by hilt to setup its own classes that can use dependency injection,
 * the annotation lets hilt attach itself to the application lifecycle.
 */

@HiltAndroidApp
class HiltApplication : Application()