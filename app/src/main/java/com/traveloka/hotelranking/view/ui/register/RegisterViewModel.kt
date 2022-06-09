package com.traveloka.hotelranking.view.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.traveloka.hotelranking.data.HotelRepository
import com.traveloka.hotelranking.data.Resource
import com.traveloka.hotelranking.data.remote.response.UserRegisterResponse

class RegisterViewModel(private val hotelRepository: HotelRepository): ViewModel() {

    fun registerUser(name: String, email: String, phone: String, password: String,
                     favCountry: String?, favFood: String?,
                     favMovie: String?): LiveData<Resource<UserRegisterResponse>> {
        return hotelRepository.registerUser(name, email, phone, password, favCountry, favFood, favMovie).asLiveData()
    }
}