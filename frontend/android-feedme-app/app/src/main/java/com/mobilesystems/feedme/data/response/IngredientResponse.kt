package com.mobilesystems.feedme.data.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class IngredientResponse(
    val ingredientId: Int,
    val ingredientName: String,
    val quantity: String = "1 Stück"
)