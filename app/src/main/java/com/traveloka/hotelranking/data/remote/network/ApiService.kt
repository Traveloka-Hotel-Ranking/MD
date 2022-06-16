package com.traveloka.hotelranking.data.remote.network

import com.traveloka.hotelranking.data.remote.response.*
import com.traveloka.hotelranking.model.HomeMLModel
import com.traveloka.hotelranking.model.param.HomeMLParam
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

    @GET("hotel/loc")
    suspend fun getHotel(
        @Header("x-access-token-hotel") token: String,
        @Query("size") size : Int
    ): Response<HotelListResponse>

    @GET("hotel")
    suspend fun getHotelMaps(
        @Header("x-access-token-hotel") token: String,
        @Query("size") size : Int
    ): Response<HotelListResponse>

    @GET("hotel")
    suspend fun getHotelByName(
        @Header("x-access-token-hotel") token: String,
        @Query("name") name : String?
    ): Response<HotelListResponse>

    @GET("hotel/loc")
    suspend fun getHotelByLocation(
        @Header("x-access-token-hotel") token: String,
        @Query("location") location : String?
    ): Response<HotelListResponse>

    @POST("projects/capstone-project-353103/locations/asia-southeast1/endpoints/9116305992588460032/:predict")
    suspend fun getHotelML(
        @Header("Authorization") token : String,
        @Body body : HomeMLParam
    ) : Response<HomeMLModel>

    @GET("hotel")
    suspend fun getHotelPaging(
        @Header("x-access-token-hotel") token: String,
        @Query("page") page : Int,
        @Query("size") size: Int,
        @Query("name") name : String
    ): HotelListResponse

}