package com.traveloka.hotelranking.data.source.remote.network

import com.traveloka.hotelranking.data.source.remote.response.UserRegisterResponse
import com.traveloka.hotelranking.data.source.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("signup")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Call<UserRegisterResponse>

    @FormUrlEncoded
    @POST("signin")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<UserResponse>
}