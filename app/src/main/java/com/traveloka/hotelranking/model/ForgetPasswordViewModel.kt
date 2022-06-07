package com.traveloka.hotelranking.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.traveloka.hotelranking.data.HotelRepository
import com.traveloka.hotelranking.data.Resource
import com.traveloka.hotelranking.data.remote.response.ForgetPasswordUserResponse
import com.traveloka.hotelranking.data.remote.response.ResetPasswordResponse
import kotlinx.coroutines.launch

class ForgetPasswordViewModel(private val preference: UserPreference, private val hotelRepository: HotelRepository) : ViewModel() {
    fun forgetPassword(email: String, favCountry: String?, favFood: String?, favMovie: String?): LiveData<Resource<ForgetPasswordUserResponse>> {
        return hotelRepository.forgetPassword(email, favCountry, favFood, favMovie).asLiveData()
    }

    fun getUserForgetPassword(): LiveData<UserForgetPasswordModel> {
        return preference.getUserForgetPassword().asLiveData()
    }

    fun saveForgetPassword(userForgetPasswordModel: UserForgetPasswordModel) {
        viewModelScope.launch {
            preference.saveUserForgetPassword(userForgetPasswordModel)
        }
    }

    fun resetPassword(tokenReset: String, email: String, newPassword: String): LiveData<Resource<ResetPasswordResponse>> {
        return hotelRepository.resetPassword(tokenReset, email, newPassword).asLiveData()
    }
}