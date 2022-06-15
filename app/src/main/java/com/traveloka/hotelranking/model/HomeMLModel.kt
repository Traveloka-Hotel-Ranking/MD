package com.traveloka.hotelranking.model


import com.google.gson.annotations.SerializedName

data class HomeMLModel(
    @SerializedName("predictions")
    val predictions: List<Prediction>
)

data class Prediction(
    @SerializedName("output_2")
    val output2: List<String>
)