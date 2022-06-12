package com.traveloka.hotelranking.data.remote.network

import com.traveloka.hotelranking.data.remote.response.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("auth/signup")
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("password") password: String,
        @Field("favCountry") favCountry: String?,
        @Field("favFood") favFood: String?,
        @Field("favMovie") favMovie: String?
    ): Response<UserRegisterResponse>

    @FormUrlEncoded
    @POST("auth/signin")
    suspend fun loginUser(
        @Field("email") email: String?,
        @Field("phone") phone: String?,
        @Field("password") password: String
    ): Response<UserResponse>

    @FormUrlEncoded
    @POST("auth/forgotpassword")
    suspend fun userForgetPassword(
        @Field("email") email: String,
        @Field("favCountry") favCountry: String?,
        @Field("favFood") favFood: String?,
        @Field("favMovie") favMovie: String?
    ): Response<ForgetPasswordUserResponse>

    @FormUrlEncoded
    @PUT("auth/resetpassword")
    suspend fun resetPassword(
        @Header("x-access-token-reset") tokenReset: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<ResetPasswordResponse>

    @GET("hotel")
    suspend fun getHotel(
        @Header("x-access-token-hotel") token: String,
        @Query("size") size: Int,
        @Query("location") location : String
    ): Response<HotelListResponse>

}