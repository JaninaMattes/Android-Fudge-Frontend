package com.mobilesystems.feedme.data.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class ImageResponse(val imageId: Int,
                         val imageName: String,
                         val imageUrl: String,
                         val base64ImageString: String? = null)
