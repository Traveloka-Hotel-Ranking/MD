package com.traveloka.hotelranking.data

import com.traveloka.hotelranking.data.remote.network.ApiService
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
                     password: String)
    : Flow<Resource<UserRegisterResponse>> = flow {
        emit(Resource.Loading())
        try {
            val client = apiService.registerUser(name, email, phone, password)
            emit(Resource.Success(client))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    fun loginUser(email: String, password: String): Flow<Resource<UserResponse>> = flow {
        emit(Resource.Loading())
        try {
            val client = apiService.loginUser(email, password)
            emit(Resource.Success(client))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }
}