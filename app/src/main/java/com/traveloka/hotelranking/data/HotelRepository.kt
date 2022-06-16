package com.traveloka.hotelranking.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.traveloka.hotelranking.data.remote.network.ApiService
import com.traveloka.hotelranking.data.remote.response.*
import com.traveloka.hotelranking.model.dummy.DummyData
import com.traveloka.hotelranking.model.dummy.HomeModel
import com.traveloka.hotelranking.view.utils.constants.MESSAGE_ERROR_REQUEST
import com.traveloka.hotelranking.view.utils.constants.SERVER_TIME_OUT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.Dispatcher
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException

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
            val response = apiService.registerUser(name, email, phone, password, favCountry, favFood, favMovie)
            if (response.isSuccessful) {
                emit(Resource.Success(response.body()))
            } else {
                val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                val message = jsonObj.getString("message")
                emit(Resource.Error(message))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Server timeout!"))
        }
    }

    fun loginUser(email: String?, phone: String?, password: String): Flow<Resource<UserResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.loginUser(email, phone, password)
            if (response.isSuccessful) {
                emit(Resource.Success(response.body()))
            } else {
                val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                val message = jsonObj.getString("message")
                emit(Resource.Error(message))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Server timeout!"))
        }
    }

    fun forgetPassword(email: String, favCountry: String?, favFood: String?, favMovie: String?): Flow<Resource<ForgetPasswordUserResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.userForgetPassword(email, favCountry, favFood, favMovie)
            if (response.isSuccessful) {
                emit(Resource.Success(response.body()))
            } else {
                val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                val message = jsonObj.getString("message")
                emit(Resource.Error(message))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Server timeout!"))
        }
    }

    fun resetPassword(tokenReset: String, email: String, newPassword: String): Flow<Resource<ResetPasswordResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.resetPassword(tokenReset, email, newPassword)
            if (response.isSuccessful) {
                emit(Resource.Success(response.body()))
            } else {
                val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                val message = jsonObj.getString("message")
                emit(Resource.Error(message))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Server timeout!"))
        }
    }

    fun retrieveHotelMaps(token : String) : Flow<Resource<HotelListResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getHotelMaps(token, 100)
            if (response.isSuccessful && response.body() !=null){
                emit(Resource.Success(response.body()))
            }else{
                val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                val message = jsonObj.getString("message")
                emit(Resource.Error(message))
            }
        }catch (e : Exception){
            emit(Resource.Error(SERVER_TIME_OUT))
        }
    }

    fun retrieveHotelSearchByName(token : String, name : String) : Flow<Resource<HotelListResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getHotelByName(token, name)
            if (response.isSuccessful && response.body() !=null){
                emit(Resource.Success(response.body()))
            }else{
                val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                val message = jsonObj.getString("message")
                emit(Resource.Error(message))
            }
        }catch (e : Exception){
            emit(Resource.Error(SERVER_TIME_OUT))
        }
    }

    fun retrieveHotelSearchByLocation(token : String, location : String) : Flow<Resource<HotelListResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getHotelByLocation(token, location)
            if (response.isSuccessful && response.body() !=null){
                emit(Resource.Success(response.body()))
            }else{
                val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                val message = jsonObj.getString("message")
                emit(Resource.Error(message))
            }
        }catch (e : Exception){
            emit(Resource.Error(SERVER_TIME_OUT))
        }
    }

    fun retrieveHotelPaging(token: String, param : String) : LiveData<PagingData<HotelItem>> {
        val pager = Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                HotelPagingSource(token, apiService, param)
            }
        ).liveData
        return pager
    }

}