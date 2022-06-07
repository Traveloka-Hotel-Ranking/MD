package com.traveloka.hotelranking.model

data class UserForgetPasswordModel(
    val email: String,
    val accessTokenReset: String
)