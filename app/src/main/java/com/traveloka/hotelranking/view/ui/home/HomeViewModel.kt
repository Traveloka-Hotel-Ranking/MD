package com.traveloka.hotelranking.view.ui.home

import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.traveloka.hotelranking.data.HomeRepository
import com.traveloka.hotelranking.data.HotelRepository
import com.traveloka.hotelranking.data.Resource
import com.traveloka.hotelranking.data.remote.response.HotelItem
import com.traveloka.hotelranking.model.HomeMLModel
import com.traveloka.hotelranking.model.UserModel
import com.traveloka.hotelranking.model.UserPreference
import com.traveloka.hotelranking.model.param.HomeMLParam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: HotelRepository,
    private val repositoryHome: HomeRepository,
    private val preference: UserPreference
) : ViewModel() {

    @ExperimentalPagingApi
    fun requestHotelPaging(token: String, param: String): LiveData<PagingData<HotelItem>> {
        return repository.retrieveHotelPaging(token, param)
            .asLiveData()
            .cachedIn(viewModelScope)
    }

    private val _isErrorRequestListName = MutableLiveData<String>()
    private val _dataRequestListName = MutableLiveData<List<HotelItem>>()
    private val _isLoadingRequestListName = MutableLiveData<Boolean>()

    val isErrorRequestListName = _isErrorRequestListName
    val dataRequestListName = _dataRequestListName
    val isLoadingRequestListName = _isLoadingRequestListName

    private val _isErrorRequestListLocation = MutableLiveData<String>()
    private val _dataRequestListLocation = MutableLiveData<List<HotelItem>>()
    private val _isLoadingRequestListLocation = MutableLiveData<Boolean>()

    val isErrorRequestListLocation = _isErrorRequestListLocation
    val dataRequestListLocation = _dataRequestListLocation
    val isLoadingRequestListLocation = _isLoadingRequestListLocation

    private val _isErrorRequestListML = MutableLiveData<String>()
    private val _dataRequestListML = MutableLiveData<HomeMLModel>()
    private val _isLoadingRequestListML = MutableLiveData<Boolean>()


    val isErrorRequestListML = _isErrorRequestListML
    val dataRequestListML = _dataRequestListML
    val isLoadingRequestListML = _isLoadingRequestListML

    private val _isErrorRequestReview = MutableLiveData<String>()
    private val _dataRequestReview = MutableLiveData<List<HotelItem>>()
    private val _isLoadingRequestReview = MutableLiveData<Boolean>()

    val isErrorRequestListReview = _isErrorRequestReview
    val dataRequestListReview = _dataRequestReview
    val isLoadingRequestReview = _isLoadingRequestReview

    private val _isErrorRequestChipReview = MutableLiveData<String>()
    private val _dataRequestChipReview = MutableLiveData<List<String>>()
    private val _isLoadingRequestChipReview = MutableLiveData<Boolean>()

    val isErrorRequestChipReview = _isErrorRequestChipReview
    val dataRequestChipReview = _dataRequestChipReview
    val isLoadingRequestChipReview = _isLoadingRequestChipReview

    fun getUser(): LiveData<UserModel> {
        return preference.getUser().asLiveData()
    }

    fun requestDataListML(token: String, param: HomeMLParam) {
        viewModelScope.launch {
            repositoryHome.retrieveHotelML(token, param)
                .onStart {
                    _isLoadingRequestListML.postValue(true)
                }
                .onCompletion {
                    _isLoadingRequestListML.postValue(false)
                }
                .collect { data ->
                    when (data) {
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
                    _isLoadingRequestListName.postValue(true)
                }
                .onCompletion {
                    _isLoadingRequestListName.postValue(false)
                }
                .collect { data ->
                    when (data) {
                        is Resource.Loading -> _isLoadingRequestListName.postValue(true)
                        is Resource.Success -> _dataRequestListName.postValue(data.data?.response?.hotel!!)
                        is Resource.Error -> _isErrorRequestListName.postValue(data.message!!)

                    }
                }
        }
    }

    fun requestDataByLocation(token: String, location: String) {
        viewModelScope.launch {
            repository.retrieveHotelSearchByLocation(token, location)
                .onStart {
                    _isLoadingRequestListLocation.postValue(true)
                }
                .onCompletion {
                    _isLoadingRequestListLocation.postValue(false)
                }
                .collect { data ->
                    when (data) {
                        is Resource.Loading -> _isLoadingRequestListLocation.postValue(true)
                        is Resource.Success -> _dataRequestListLocation.postValue(data.data?.response?.hotel!!)
                        is Resource.Error -> _isErrorRequestListLocation.postValue(data.message!!)

                    }
                }
        }
    }

    fun requestHotelReview(token: String, review: Double, reviewMax: Double) {
        viewModelScope.launch {
            repository.requestHotelByReview(token, review, reviewMax)
                .onStart {
                    _isLoadingRequestReview.postValue(true)
                }
                .onCompletion {
                    _isLoadingRequestReview.postValue(false)
                }
                .collect { data ->
                    when (data) {
                        is Resource.Loading -> _isLoadingRequestReview.postValue(true)
                        is Resource.Success -> _dataRequestReview.postValue(data.data?.data)
                        is Resource.Error -> _isErrorRequestReview.postValue(data.message!!)

                    }
                }
        }
    }

    fun requestListReview(){
        viewModelScope.launch {
            repository.requestListReview()
                .onStart {
                    _isLoadingRequestChipReview.postValue(true)
                }
                .onCompletion {
                    _isLoadingRequestChipReview.postValue(false)
                }
                .collect { data ->
                    when (data) {
                        is Resource.Loading -> _isLoadingRequestChipReview.postValue(true)
                        is Resource.Success -> _dataRequestChipReview.postValue(data.data!!)
                        is Resource.Error -> _isErrorRequestChipReview.postValue(data.message!!)

                    }
                }
        }
    }

}