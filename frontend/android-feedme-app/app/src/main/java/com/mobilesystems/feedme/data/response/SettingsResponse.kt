package com.mobilesystems.feedme.data.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class SettingsResponse(
    val settingsId: Int,
    val remindBeforeProductExpiration: Boolean? = null,
    val allowPushNotifications: Boolean? = null,
    val suggestProductsForShoppingList: Boolean? = null,
)