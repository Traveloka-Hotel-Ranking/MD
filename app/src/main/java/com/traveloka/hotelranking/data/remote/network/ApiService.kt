package com.traveloka.hotelranking.data.remote.network

import com.traveloka.hotelranking.data.remote.response.UserRegisterResponse
import com.traveloka.hotelranking.data.remote.response.UserResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("signup")
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("password") password: String
    ): UserRegisterResponse

    @FormUrlEncoded
    @POST("signin")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): UserResponse
}