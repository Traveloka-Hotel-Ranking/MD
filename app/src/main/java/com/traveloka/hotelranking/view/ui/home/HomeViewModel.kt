package com.traveloka.hotelranking.view.ui.home

import androidx.lifecycle.*
import com.traveloka.hotelranking.data.HomeRepository
import com.traveloka.hotelranking.data.HotelRepository
import com.traveloka.hotelranking.data.Resource
import com.traveloka.hotelranking.data.remote.response.HotelItem
import com.traveloka.hotelranking.data.remote.response.HotelListResponse
import com.traveloka.hotelranking.model.HomeMLModel
import com.traveloka.hotelranking.model.UserModel
import com.traveloka.hotelranking.model.UserPreference
import com.traveloka.hotelranking.model.dummy.HomeModel
import com.traveloka.hotelranking.model.param.HomeMLParam
import com.traveloka.hotelranking.model.param.HotelListParam
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: HotelRepository,
    private val repositoryHome : HomeRepository,
    private val preference: UserPreference
    ) : ViewModel() {

    private val _isErrorRequestList = MutableLiveData<String>()
    private val _dataRequestList = MutableLiveData<List<HotelItem>>()
    private val _isLoadingRequestList = MutableLiveData<Boolean>()

    val isErrorRequestList = _isErrorRequestList
    val dataRequestList = _dataRequestList
    val isLoadingRequestList = _isLoadingRequestList

    private val _isErrorRequestListML = MutableLiveData<String>()
    private val _dataRequestListML = MutableLiveData<HomeMLModel>()
    private val _isLoadingRequestListML = MutableLiveData<Boolean>()

    val isErrorRequestListML = _isErrorRequestListML
    val dataRequestListML = _dataRequestListML
    val isLoadingRequestListML = _isLoadingRequestListML

    fun getUser(): LiveData<UserModel> {
        return preference.getUser().asLiveData()
    }

    fun requestDataList(token : String){
        viewModelScope.launch {
            repository.retrieveHotel(token)
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
    fun requestDataListML(token : String, param : HomeMLParam){
        viewModelScope.launch {
            repositoryHome.retrieveHotelML(token, param)
                .onStart {
                    _isLoadingRequestList.postValue(true)
                }
                .onCompletion {
                    _isLoadingRequestList.postValue(false)
                }
                .collect{ data ->
                    when(data){
                        is Resource.Loading -> _isLoadingRequestListML.postValue(true)
                        is Resource.Success -> _dataRequestListML.postValue(data.data!!)
                        is Resource.Error -> _isErrorRequestListML.postValue(data.message!!)
                    }
                }
        }
    }

    fun requestDataByName(token: String, name: String) {
        viewModelScope.launch {
            repository.retrieveHotelSearchByName(token, name)
                .onStart {
                    _isLoadingRequestList.postValue(true)
                }
                .onCompletion {
                    _isLoadingRequestList.postValue(false)
                }
                .collect { data ->
                    when (data) {
                        is Resource.Loading -> _isLoadingRequestList.postValue(true)
                        is Resource.Success -> _dataRequestList.postValue(data.data?.response?.hotel!!)
                        is Resource.Error -> _isErrorRequestList.postValue(data.message!!)

                    }
                }
        }
    }

    fun requestDataByLocation(token: String, location: String) {
        viewModelScope.launch {
            repository.retrieveHotelSearchByLocation(token, location)
                .onStart {
                    _isLoadingRequestList.postValue(true)
                }
                .onCompletion {
                    _isLoadingRequestList.postValue(false)
                }
                .collect { data ->
                    when (data) {
                        is Resource.Loading -> _isLoadingRequestList.postValue(true)
                        is Resource.Success -> _dataRequestList.postValue(data.data?.response?.hotel!!)
                        is Resource.Error -> _isErrorRequestList.postValue(data.message!!)

                    }
                }
        }
    }
}