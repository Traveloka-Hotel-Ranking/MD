package com.traveloka.hotelranking.view.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.traveloka.hotelranking.data.HotelRepository
import com.traveloka.hotelranking.data.Resource
import com.traveloka.hotelranking.model.dummy.HomeModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HotelRepository) : ViewModel() {

    private val _isErrorRequestList = MutableLiveData<String>()
    private val _dataRequestList = MutableLiveData<List<HomeModel>>()
    private val _isLoadingRequestList = MutableLiveData<Boolean>()

    val isErrorRequestList = _isErrorRequestList
    val dataRequestList = _dataRequestList
    val isLoadingRequestList = _isLoadingRequestList

    fun requestDataList(){
        viewModelScope.launch {
            repository.retrieveHotel()
                .onStart {
                    _isLoadingRequestList.postValue(true)
                }
                .onCompletion {
                    _isLoadingRequestList.postValue(false)
                }
                .collect{ data ->
                    when(data){
                        is Resource.Loading -> _isLoadingRequestList.postValue(true)
                        is Resource.Success -> _dataRequestList.postValue(data.data!!)
                        is Resource.Error -> _isErrorRequestList.postValue(data.message!!)

                    }
                }
        }
    }
}