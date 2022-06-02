package com.traveloka.hotelranking.model

data class UserModel(
    val name: String,
    val email: String,
    val accessToken: String,
    val checkLogin: Boolean
)