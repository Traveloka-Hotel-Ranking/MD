package com.traveloka.hotelranking.model

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.traveloka.hotelranking.data.source.remote.network.ApiConfig
import com.traveloka.hotelranking.data.source.remote.response.UserRegisterResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel: ViewModel() {

    private val _signup = MutableLiveData<UserRegisterResponse>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var messageResponse = MutableLiveData<String?>()
    var messageSuccessResponse = MutableLiveData<String?>()

    fun registerUser(name: String, email: String, phone: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().registerUser(name, email, phone, password)
        client.enqueue(object : Callback<UserRegisterResponse> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<UserRegisterResponse>,
                response: Response<UserRegisterResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _signup.value = responseBody!!

                    val message = _signup.value?.message.toString()
                    messageSuccessResponse.value = message
                } else {
                    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                    messageResponse.value = jsonObj.getString("message")
                }
            }

            @SuppressLint("LogNotTimber")
            override fun onFailure(call: Call<UserRegisterResponse>, t: Throwable) {
                _isLoading.value = false
                messageResponse.value = "Server Timeout!"
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}