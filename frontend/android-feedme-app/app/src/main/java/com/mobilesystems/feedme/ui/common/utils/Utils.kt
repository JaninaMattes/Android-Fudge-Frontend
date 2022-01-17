package com.mobilesystems.feedme.ui.common.utils

import android.app.Activity
import android.app.Application
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
import android.os.Build
import android.util.Log
import com.mobilesystems.feedme.data.response.UserResponse
import com.mobilesystems.feedme.domain.model.FoodType
import com.mobilesystems.feedme.domain.model.User
import com.mobilesystems.feedme.ui.authentication.LoggedInUser
import org.json.JSONObject

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
        "$header"
        "$payload"
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

fun getLoggedInUser(context: Context) : LoggedInUser? {
    //Retrieve current user id stored in preference
    val loggedInUser = getSavedObjectFromPreference(context, "mPreference",
        "loggedInUser", LoggedInUser::class.java)
    if (loggedInUser != null) {
        Log.d("SharedPreferences", "Retrieved user with Id ${loggedInUser.userId} from shared preferences!")
    }
    return loggedInUser
}

fun getJWTToken(context: Context) : String? {
    //Retrieve current user id stored in preference
    val jwt = getSavedObjectFromPreference(context, "mPreference",
        "jwtToken", String::class.java)
    if (jwt != null) {
        Log.d("SharedPreferences", "Retrieved jwt token $jwt from shared preferences!")
    }
    return jwt
}


fun convertUserResponse(userResponse: UserResponse?) : User? {
    var user: User? = null
    var dietaryPreferences: MutableList<FoodType>? = null
    if(userResponse != null) {
        Log.d("Utils", "Convert UserResponse object to User.")
        if (userResponse.dietaryPreferences.isNotEmpty()) {
            for (tag in userResponse.dietaryPreferences) {
                val dietPref = FoodType.from(tag)
                if (dietPref != null) {
                    dietaryPreferences?.add(dietPref)
                }
            }
        }
        user = User(
            userId = userResponse.userId,
            firstName = userResponse.firstName,
            lastName = userResponse.lastName,
            email = userResponse.email,
            password = userResponse.password,
            userSettings = userResponse.userSettings,
            dietaryPreferences = dietaryPreferences,
            userImage = userResponse.imgUrl
        )
    }else{
        Log.d("Utils", "UserResponse is null!")
    }
    return user
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
