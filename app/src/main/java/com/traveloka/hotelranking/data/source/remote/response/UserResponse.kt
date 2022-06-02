package com.traveloka.hotelranking.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @field:SerializedName("roles")
    val roles: List<String>,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("accessToken")
    val accessToken: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("status")
    val status: String
)

data class UserRegisterResponse(
    @field:SerializedName("message")
    val message: String
)
