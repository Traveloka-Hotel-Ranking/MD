package com.traveloka.hotelranking.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.traveloka.hotelranking.data.HotelRepository
import com.traveloka.hotelranking.data.Resource
import com.traveloka.hotelranking.data.remote.response.UserRegisterResponse
import kotlinx.coroutines.flow.Flow

class RegisterViewModel(private val hotelRepository: HotelRepository): ViewModel() {

    private val _signup = MutableLiveData<UserRegisterResponse>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var messageResponse = MutableLiveData<String?>()
    var messageSuccessResponse = MutableLiveData<String?>()

    fun registerUser(name: String, email: String, phone: String, password: String): LiveData<Resource<UserRegisterResponse>> {
        return hotelRepository.registerUser(name, email, phone, password).asLiveData()

//        _isLoading.value = true
//        val client = ApiConfig.getApiService().registerUser(name, email, phone, password)
//        client.enqueue(object : Callback<UserRegisterResponse> {
//            @SuppressLint("LogNotTimber")
//            override fun onResponse(
//                call: Call<UserRegisterResponse>,
//                response: Response<UserRegisterResponse>
//            ) {
//                _isLoading.value = false
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    _signup.value = responseBody!!
//
//                    val message = _signup.value?.message.toString()
//                    messageSuccessResponse.value = message
//                } else {
//                    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
//                    messageResponse.value = jsonObj.getString("message")
//                }
//            }
//
//            @SuppressLint("LogNotTimber")
//            override fun onFailure(call: Call<UserRegisterResponse>, t: Throwable) {
//                _isLoading.value = false
//                messageResponse.value = "Server Timeout!"
//                Log.e(TAG, "onFailure: ${t.message}")
//            }
//
//        })
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}