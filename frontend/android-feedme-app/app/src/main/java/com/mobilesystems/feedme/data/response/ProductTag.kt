package com.mobilesystems.feedme.data.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class ProductTag(
    val tagId: Int,
    val label: String
)