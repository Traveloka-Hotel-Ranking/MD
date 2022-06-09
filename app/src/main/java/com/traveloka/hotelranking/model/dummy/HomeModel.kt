package com.traveloka.hotelranking.model.dummy

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeModel(
    val title : String,
    val rating : String,
    val currentLocation : String,
    val ratingHotel : String,
    val discount : String,
    val price : String,
    val pricePerNight : String,
    val point : String,
    val image : List<ImageModel>,
    val room: List<RoomModel>
): Parcelable