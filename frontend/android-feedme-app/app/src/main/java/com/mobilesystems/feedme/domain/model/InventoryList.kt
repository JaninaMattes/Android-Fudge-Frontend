package com.mobilesystems.feedme.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InventoryList(
    val inventoryListId: Int,
    val inventoryList: List<Product>? = null
) : Parcelable {

}
