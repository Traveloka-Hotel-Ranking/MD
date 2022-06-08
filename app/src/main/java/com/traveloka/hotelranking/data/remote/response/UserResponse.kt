package com.traveloka.hotelranking.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @field:SerializedName("roles")
    val roles: List<String>,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("phone")
    val phone: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("accessToken")
    val accessToken: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("favCountry")
    val favCountry: String?,

    @field:SerializedName("favMovie")
    val favMovie: String?,

    @field:SerializedName("favFood")
    val favFood: String?

)

data class UserRegisterResponse(
    @field:SerializedName("message")
    val message: String
)

data class ForgetPasswordUserResponse(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("roles")
    val roles: List<String>,

    @field:SerializedName("accessToken")
    val accessTokenPassword: String
)

data class ResetPasswordResponse(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String
)
