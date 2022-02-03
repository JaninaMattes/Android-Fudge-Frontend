package com.mobilesystems.feedme.ui.common.utils

import android.icu.text.SimpleDateFormat
import android.util.Log
import java.util.*

class DateUtils {

    fun convertStringToDate(dateStr: String): Date {
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val date = sdf.parse(dateStr)
        Log.d("SharedInventoryViewModel", "Current date $date")
        return date
    }
}