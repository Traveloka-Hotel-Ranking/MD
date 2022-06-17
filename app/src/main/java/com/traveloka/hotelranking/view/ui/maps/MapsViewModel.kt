package com.traveloka.hotelranking.view.ui.maps

import androidx.lifecycle.*
import com.traveloka.hotelranking.data.HotelRepository
import com.traveloka.hotelranking.data.Resource
import com.traveloka.hotelranking.data.remote.response.HotelItem
import com.traveloka.hotelranking.model.UserModel
import com.traveloka.hotelranking.model.UserPreference
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MapsViewModel(
    private val repository: HotelRepository,
    private val preference: UserPreference
) : ViewModel(){
    private val _isErrorRequestList = MutableLiveData<String>()
    private val _dataRequestList = MutableLiveData<List<HotelItem>>()
    private val _isLoadingRequestList = MutableLiveData<Boolean>()

    val isErrorRequestList = _isErrorRequestList
    val dataRequestList = _dataRequestList

    fun observeGetUser(): LiveData<UserModel> {
        return preference.getUser().asLiveData()
    }

    fun requestHotelList(token : String){
        viewModelScope.launch {
            repository.retrieveHotelMaps(token)
                .onStart {
                    _isLoadingRequestList.postValue(true)
                }
                .onCompletion {
                    _isLoadingRequestList.postValue(false)
                }
                .collect{ data ->
                    when(data){
                        is Resource.Loading -> _isLoadingRequestList.postValue(true)
                        is Resource.Success -> _dataRequestList.postValue(data.data?.response?.hotel!!)
                        is Resource.Error -> _isErrorRequestList.postValue(data.message!!)
                    }
                }
        }
    }
}