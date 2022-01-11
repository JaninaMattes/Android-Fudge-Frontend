package com.mobilesystems.feedme.ui.common.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject

/**
 * Helperclass:
 * In Kotlin, all coroutines run inside a CoroutineScope. A scope controls the lifetime of coroutines through its job.
 */
@HiltViewModel
open class BaseViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    // Default dispatcher Main, this coroutine will be launched in main thread
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    protected val context
        get() = getApplication<Application>()

    // onCleared is called when the ViewModel is no longer used and will be destroyed.
    override fun onCleared() {
        super.onCleared()
        // All coroutines started in the same scope as the job are cancelled.
        viewModelJob.cancel()
    }
}