package com.traveloka.hotelranking.model


import com.google.gson.annotations.SerializedName
import com.traveloka.hotelranking.data.remote.response.HotelItem

data class ReviewResponses(
    @SerializedName("data")
    val `data`: List<HotelItem>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)