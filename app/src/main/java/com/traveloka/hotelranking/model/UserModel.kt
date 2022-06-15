package com.traveloka.hotelranking.model

data class UserModel(
    val id : String,
    val name: String,
    val email: String,
    val phone: String,
    val favCountry: String,
    val favFood: String,
    val favMovie: String,
    val accessToken: String,
    val checkLogin: Boolean
)