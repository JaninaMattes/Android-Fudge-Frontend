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
import android.icu.text.DateFormat
import android.net.ParseException
import android.os.Build
import android.util.Log
import com.mobilesystems.feedme.data.request.*
import com.mobilesystems.feedme.data.response.*
import com.mobilesystems.feedme.domain.model.*
import com.mobilesystems.feedme.domain.model.LoggedInUser
import org.json.JSONObject
import java.io.ByteArrayOutputStream

/**
 * All utility functions that are commonly used.
 */

fun convertTokenToUser(context: Context, jwt: String?): LoggedInUser? {
    var user: LoggedInUser? = null
    if(jwt != null){
        val decoded = decodeJWTToken(jwt)
        Log.d("Decoded token", decoded)
        val jsonObj = JSONObject(decoded)
        user = LoggedInUser(
            userId = jsonObj.get("userId") as Int,
            firstName = jsonObj.get("firstName") as String,
            lastName = jsonObj.get("lastName") as String,
            email = jsonObj.get("email") as String
        )
        saveTokenToSharedPreference(context, jwt)
        saveLoggedInUserToSharedPreference(context, user)
    }
    return user
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

// Remove object from shared preferences
fun removeObjectFromSharedPreference(
    context: Context,
    preferenceFileName: String?,
    serializedObjectKey: String?,
    `object` : Any?
) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(preferenceFileName, 0)
    val sharedPreferencesEditor = sharedPreferences.edit()
    sharedPreferencesEditor.remove(serializedObjectKey)
    sharedPreferencesEditor.apply()
    Log.d("SharedPreferences", "Removed $`object` from shared preferences!")
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

fun decodeJWTToken(jwt: String): String {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return "Requires SDK 26"
    val parts = jwt.split(".")
    return try {
        val charset = charset("UTF-8")
        val header = String(Base64.getUrlDecoder().decode(parts[0].toByteArray(charset)), charset)
        val payload = String(Base64.getUrlDecoder().decode(parts[1].toByteArray(charset)), charset)
        header
        payload
    } catch (e: Exception) {
        "Error parsing JWT: $e"
    }
}

fun saveTokenToSharedPreference(context: Context, jwt: String){
    saveObjectToSharedPreference(context,
        "mPreference",
        "jwtToken", jwt)
}

fun saveLoggedInUserToSharedPreference(context: Context, user: LoggedInUser){
    saveObjectToSharedPreference(context,
        "mPreference",
        "loggedInUser", user)
}

fun removeLoggedInUserFromSharedPreferences(context: Context, user: LoggedInUser){
    removeObjectFromSharedPreference(context,
        "mPreference",
        "loggedInUser", user)
}

fun removeTokenFromSharedPreferences(context: Context, jwt: String){
    removeObjectFromSharedPreference(context,
        "mPreference",
        "jwtToken", jwt)
}

fun removeAllValuesFromSharedPreferences(context: Context, preferenceFileName: String = "mPreference"){
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(preferenceFileName, 0)
    val sharedPreferencesEditor = sharedPreferences.edit()
    sharedPreferencesEditor.clear()
    sharedPreferencesEditor.apply()
    Log.d("SharedPreferences", "All entries deleted!")
}

fun getLoggedInUser(context: Context) : LoggedInUser? {
    //Retrieve current user id stored in preference
    val loggedInUser = getSavedObjectFromPreference(context, "mPreference",
        "loggedInUser", LoggedInUser::class.java)
    Log.d("SharedPreferences", "Retrieved user with id ${loggedInUser?.userId} from shared preferences!")
    return loggedInUser
}

fun getJWTToken(context: Context) : String? {
    //Retrieve current user id stored in preference
    val jwt = getSavedObjectFromPreference(context, "mPreference",
        "jwtToken", String::class.java)
    Log.d("SharedPreferences", "Retrieved jwt token $jwt from shared preferences!")
    return jwt
}

fun <A : Activity> Activity.startNewActivity(activity: Class<A>) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}

//Returns `true` if this string is empty or consists solely of whitespace characters.
fun CharSequence.isBlank(): Boolean = length == 0 || indices.all { this[it].isWhitespace() }



