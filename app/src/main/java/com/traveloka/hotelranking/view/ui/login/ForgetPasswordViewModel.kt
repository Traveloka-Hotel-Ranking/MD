package com.traveloka.hotelranking.view.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.traveloka.hotelranking.data.HotelRepository
import com.traveloka.hotelranking.data.Resource
import com.traveloka.hotelranking.data.remote.response.ForgetPasswordUserResponse
import com.traveloka.hotelranking.data.remote.response.ResetPasswordResponse

class ForgetPasswordViewModel(private val hotelRepository: HotelRepository) : ViewModel() {
    fun forgetPassword(email: String, favCountry: String?, favFood: String?, favMovie: String?): LiveData<Resource<ForgetPasswordUserResponse>> {
        return hotelRepository.forgetPassword(email, favCountry, favFood, favMovie).asLiveData()
    }

    fun resetPassword(tokenReset: String, email: String, newPassword: String): LiveData<Resource<ResetPasswordResponse>> {
        return hotelRepository.resetPassword(tokenReset, email, newPassword).asLiveData()
    }
}