package com.traveloka.hotelranking.data.remote.network

import com.traveloka.hotelranking.data.remote.response.ForgetPasswordUserResponse
import com.traveloka.hotelranking.data.remote.response.ResetPasswordResponse
import com.traveloka.hotelranking.data.remote.response.UserRegisterResponse
import com.traveloka.hotelranking.data.remote.response.UserResponse
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("signup")
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("password") password: String,
        @Field("favCountry") favCountry: String?,
        @Field("favFood") favFood: String?,
        @Field("favMovie") favMovie: String?
    ): UserRegisterResponse

    @FormUrlEncoded
    @POST("signin")
    suspend fun loginUser(
        @Field("email") email: String?,
        @Field("phone") phone: String?,
        @Field("password") password: String
    ): UserResponse

    @FormUrlEncoded
    @POST("forgotpassword")
    suspend fun userForgetPassword(
        @Field("email") email: String,
        @Field("favCountry") favCountry: String?,
        @Field("favFood") favFood: String?,
        @Field("favMovie") favMovie: String?
    ): ForgetPasswordUserResponse

    @FormUrlEncoded
    @PUT("resetpassword")
    suspend fun resetPassword(
        @Header("x-access-token-reset") tokenReset: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): ResetPasswordResponse

}