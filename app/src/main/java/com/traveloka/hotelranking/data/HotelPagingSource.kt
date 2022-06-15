package com.traveloka.hotelranking.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.traveloka.hotelranking.data.remote.response.HotelItem
import com.traveloka.hotelranking.model.param.HotelListParam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//class HotelPagingSource(
//    val token : String,
//    val repository: HotelRepository,
//    val query : HotelListParam
//    val dataItemHotel : (HotelItem) -> Unit
//) : PagingSource<Int, HotelItem>() {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HotelItem> {
//        val idx = params.key ?: 1
//        query.page = idx
//        val result = withContext(Dispatchers.IO) {
//            repository.retrieveHotel(token, query.toClean())
//        }
//
//        return when(result) {
//            is Resource.Success<*> -> {
//                val data = result.data
//                dataItemHotel.invoke(data.toModel(query.q))
//
//                LoadResult.Page(
//                    data = data.items?.data?.map { it.toModel() } ?: listOf(MarketplaceMapper.emptyDataItems()),
//                    prevKey = if (data.items?.previousPage.isNullOrEmpty()) null else query.page - 1,
//                    nextKey = if (paginationEnable == true) if (data.items?.data.isNullOrEmpty()) null else query.page + 1 else null
//                )
//            }
//            is ApiResult.Error -> {
//                LoadResult.Error(result.exception)
//            }
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, HotelItem>): Int? {
//        TODO("Not yet implemented")
//    }
//
//
//}
class HotelPagingSource {

}