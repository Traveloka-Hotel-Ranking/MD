package com.traveloka.hotelranking.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.traveloka.hotelranking.data.remote.network.ApiService
import com.traveloka.hotelranking.data.remote.response.HotelItem
import com.traveloka.hotelranking.data.remote.response.HotelListResponse

class HotelPagingSource(
    val token: String,
    val apiService: ApiService,
    val param : String
) : PagingSource<Int, HotelItem>(){

    private companion object {
        const val INITIAL_PAGE_INDEX = 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HotelItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val result : HotelListResponse = apiService.getHotelPaging(token, page, 10)
            LoadResult.Page(
                data = result.response?.hotel ?: emptyList(),
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (result.response?.hotel.isNullOrEmpty()) null else page + 1
            )
        }catch (e : Exception){
            return LoadResult.Error(e)

        }
    }

    override fun getRefreshKey(state: PagingState<Int, HotelItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}