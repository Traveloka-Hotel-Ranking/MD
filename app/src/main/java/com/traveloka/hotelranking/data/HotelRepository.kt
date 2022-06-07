package com.traveloka.hotelranking.data

import com.traveloka.hotelranking.data.remote.network.ApiService
import com.traveloka.hotelranking.data.remote.response.ForgetPasswordUserResponse
import com.traveloka.hotelranking.data.remote.response.ResetPasswordResponse
import com.traveloka.hotelranking.data.remote.response.UserRegisterResponse
import com.traveloka.hotelranking.data.remote.response.UserResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class HotelRepository(
    private val apiService: ApiService
) {
    fun registerUser(name: String,
                     email: String,
                     phone: String,
                     password: String,
                     favCountry: String?,
                     favFood: String?,
                     favMovie: String?)
    : Flow<Resource<UserRegisterResponse>> = flow {
        emit(Resource.Loading())
        try {
            val client = apiService.registerUser(name, email, phone, password, favCountry, favFood, favMovie)
            emit(Resource.Success(client))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    fun loginUser(email: String?, phone: String?, password: String): Flow<Resource<UserResponse>> = flow {
        emit(Resource.Loading())
        try {
            val client = apiService.loginUser(email, phone, password)
            emit(Resource.Success(client))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    fun forgetPassword(email: String, favCountry: String?, favFood: String?, favMovie: String?): Flow<Resource<ForgetPasswordUserResponse>> = flow {
        emit(Resource.Loading())
        try {
            val client = apiService.userForgetPassword(email, favCountry, favFood, favMovie)
            emit(Resource.Success(client))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    fun resetPassword(tokenReset: String, email: String, newPassword: String): Flow<Resource<ResetPasswordResponse>> = flow {
        emit(Resource.Loading())
        try {
            val client = apiService.resetPassword(tokenReset, email, newPassword)
            emit(Resource.Success(client))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }
}