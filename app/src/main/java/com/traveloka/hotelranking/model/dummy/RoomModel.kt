package com.traveloka.hotelranking.model.dummy

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RoomModel (
    val image: Int,
    val type: String,
    val guest: Int,
    val bedNumber: Int,
    val bedType: String,
    val breakfast: Boolean,
    val wifi: Boolean,
    val price: String,
    val discount: Boolean,
    val discountPrice: String
): Parcelable