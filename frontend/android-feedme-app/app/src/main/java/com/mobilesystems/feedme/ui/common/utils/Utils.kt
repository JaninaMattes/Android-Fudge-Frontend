package com.mobilesystems.feedme.ui.common.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.Canvas
import java.io.File
import android.graphics.drawable.BitmapDrawable

import android.graphics.drawable.Drawable
import android.icu.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import com.google.gson.Gson

import android.content.SharedPreferences
import android.util.Log
import com.mobilesystems.feedme.ui.authentication.LoggedInUser

/**
 * All utility functions.
 */

fun getTimeDiff(dateStr: String): Long {
    var result: Long = 0
    if(dateStr.isNotEmpty()) {
        val myFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.GERMANY)
        val endDateValue: Date = sdf.parse(dateStr)
        val startDateValue = Calendar.getInstance().timeInMillis

        result = TimeUnit.MILLISECONDS.toDays(endDateValue.time - startDateValue)
    }
    return result
}

fun addDaysToCurrentDate(days: Int): Calendar {
    val currentDate = getCurrentDate()
    currentDate.add(Calendar.DATE, days)
    return currentDate
}

fun getCurrentDate(): Calendar {
    val calendar =  Calendar.getInstance()
    val currentDate = calendar.time
    calendar.time = currentDate
    return calendar
}

// Save objects to shared preferences
fun saveObjectToSharedPreference(
    context: Context,
    preferenceFileName: String?,
    serializedObjectKey: String?,
    `object` : Any?
) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(preferenceFileName, 0)
    val sharedPreferencesEditor = sharedPreferences.edit()
    val gson = Gson()
    val serializedObject = gson.toJson(`object`)
    sharedPreferencesEditor.putString(serializedObjectKey, serializedObject)
    sharedPreferencesEditor.apply()
    Log.d("SharedPreferences", "Saved $`object` to shared preferences!")
}

// Retrieve object from shared preferences
fun <GenericClass> getSavedObjectFromPreference(
    context: Context,
    preferenceFileName: String?,
    preferenceKey: String?,
    classType: Class<GenericClass>?
): GenericClass? {
    val sharedPreferences = context.getSharedPreferences(preferenceFileName, 0)
    if (sharedPreferences.contains(preferenceKey)) {
        val gson = Gson()
        Log.d("SharedPreferences", "Retrieved object from shared preferences!")
        return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType)
    }
    return null
}

fun getLoggedInUser(context: Context) : Int? {
    //Retrieve current user id stored in preference
    val loggedInUsr = getSavedObjectFromPreference(context, "mPreference",
        "loggedInUser", LoggedInUser::class.java)
    if (loggedInUsr != null) {
        Log.d("SharedPreferences", "Retrieved user with Id ${loggedInUsr.userId} from shared preferences!")
    }
    return loggedInUsr?.userId
}

fun filePathToBitmap(pathname: String): Bitmap {
    val imgFile = File(pathname)
    return BitmapFactory.decodeFile(imgFile.getAbsolutePath())
}

fun drawableToBitmap(drawable: Drawable): Bitmap {
    var bitmap: Bitmap? = null
    if (drawable is BitmapDrawable) {
        if (drawable.bitmap != null) {
            return drawable.bitmap
        }
    }
    bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
        Bitmap.createBitmap(
            1,
            1,
            Bitmap.Config.ARGB_8888
        ) // Single color bitmap will be created of 1x1 pixel
    } else {
        Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
    }
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
    drawable.draw(canvas)
    return bitmap
}

fun<A: Activity> Activity.startNewActivity(activity: Class<A>){
    Intent(this, activity).also{
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}
