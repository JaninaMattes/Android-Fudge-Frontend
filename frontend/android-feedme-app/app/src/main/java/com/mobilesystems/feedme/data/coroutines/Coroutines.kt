package com.mobilesystems.feedme.data.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object Coroutines {

    fun <T: Any> ioThenMain(work: suspend(() -> T?), callback: ((T?) -> Unit)) =

        CoroutineScope(Dispatchers.Main).launch {
            //fetch data from database in IO thread
            val data =
                withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                    work()
                }
            callback(data)
        }

}