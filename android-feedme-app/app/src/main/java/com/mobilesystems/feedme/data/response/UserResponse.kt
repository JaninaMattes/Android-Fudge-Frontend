package com.mobilesystems.feedme.data.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Map response jsonObject to UserResponse data class.
 */
data class UserResponse (
    val userId : Int,
    val firstName : String,
    val lastName : String,
    val email : String,
    val password : String,
    val userSettings : SettingsResponse?,
    val userImage : ImageResponse? = null,
    val dietaryPreferences : List<ProductTag>? = null)