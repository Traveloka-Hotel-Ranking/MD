package com.traveloka.hotelranking.model

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.*
import com.traveloka.hotelranking.data.HotelRepository
import com.traveloka.hotelranking.data.Resource
import com.traveloka.hotelranking.data.remote.network.ApiService
import com.traveloka.hotelranking.data.remote.response.UserResponse
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val preference: UserPreference,
                     private val hotelRepository: HotelRepository)
    : ViewModel() {
    private val _signing = MutableLiveData<UserResponse>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var messageResponse = MutableLiveData<String?>()
    var messageSuccessResponse = MutableLiveData<String?>()

    fun loginUser(email: String, password: String): LiveData<Resource<UserResponse>> {
        return hotelRepository.loginUser(email, password).asLiveData()

//        _isLoading.value = true
//        val client = ApiConfig.getApiService().loginUser(email, password)
//        client.enqueue(object : Callback<UserResponse> {
//            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
//                _isLoading.value = false
//                if (response.isSuccessful) {
//                    login()
//                    val responseBody = response.body()
//                    _signing.value = responseBody!!
//
//                    val name = _signing.value?.name.toString()
//                    val email = _signing.value?.email.toString()
//                    val accessToken = _signing.value?.accessToken.toString()
//
//                    saveUser(UserModel(name, email, accessToken, true))
//
//                    val message = _signing.value?.message.toString()
//                    messageSuccessResponse.value = message
//                } else {
//                    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
//                    messageResponse.value = jsonObj.getString("message")
//                }
//            }
//
//            @SuppressLint("LogNotTimber")
//            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
//                _isLoading.value = false
//                messageResponse.value = "Server Timeout!"
//                Log.e(TAG, "onFailure: ${t.message}")
//            }
//
//        })
    }

    fun getUser(): LiveData<UserModel> {
        return preference.getUser().asLiveData()
    }

    fun saveUser(userModel: UserModel) {
        viewModelScope.launch {
            preference.saveUser(userModel)
        }
    }

    fun login() {
        viewModelScope.launch {
            preference.login()
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}