package com.traveloka.hotelranking.data

import com.traveloka.hotelranking.data.remote.network.ApiService
import com.traveloka.hotelranking.model.HomeMLModel
import com.traveloka.hotelranking.model.param.HomeMLParam
import com.traveloka.hotelranking.view.utils.constants.SERVER_TIME_OUT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject

class HomeRepository(
    private val apiService: ApiService
) {
    fun retrieveHotelML(token : String, param : HomeMLParam) : Flow<Resource<HomeMLModel>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getHotelML(token, param)
            if (response.isSuccessful && response.body() !=null){
                emit(Resource.Success(response.body()))
            }else{
                val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                val message = jsonObj.getString("message")
                emit(Resource.Error(message))
            }
        }catch (e : Exception){
            emit(Resource.Error(SERVER_TIME_OUT))
        }
    }
}