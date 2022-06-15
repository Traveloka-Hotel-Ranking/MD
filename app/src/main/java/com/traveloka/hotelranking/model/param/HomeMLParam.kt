package com.traveloka.hotelranking.model.param


import com.google.gson.annotations.SerializedName

data class HomeMLParam(
    @SerializedName("instances")
    val instances: List<Instance>
)

data class Instance(
    @SerializedName("input_1")
    val input1: String
)