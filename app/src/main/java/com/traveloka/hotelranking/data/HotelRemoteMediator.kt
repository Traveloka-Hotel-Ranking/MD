package com.traveloka.hotelranking.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.traveloka.hotelranking.data.local.entity.HotelEntity
import com.traveloka.hotelranking.data.local.entity.RemoteKeysEntity
import com.traveloka.hotelranking.data.local.room.HotelDatabase
import com.traveloka.hotelranking.data.remote.network.ApiService
import com.traveloka.hotelranking.data.remote.response.HotelItem

@ExperimentalPagingApi
class HotelRemoteMediator(
    private val hotelDatabase: HotelDatabase,
    private val apiService: ApiService,
    private val token: String
) : RemoteMediator<Int, HotelEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, HotelEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = false)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
                nextKey
            }
        }

        return try {
            val responseData = apiService.getHotelPaging(token, page, state.config.pageSize)

            val endOfPaginationReached =
                responseData.response?.currentPage == responseData.response?.totalPages
                        || responseData.response?.hotel?.isEmpty() == true

            hotelDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    hotelDatabase.remoteKeysDao().deleteRemoteKeys()
                    hotelDatabase.hotelDao().deleteAll()
                }
                val prevKey = if (page == 0) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = responseData.response?.hotel?.map {
                    RemoteKeysEntity(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                if (keys != null) hotelDatabase.remoteKeysDao().insertAll(keys)

                val hotelEntity = mapper(responseData.response?.hotel)

                hotelDatabase.hotelDao().insertHotel(hotelEntity)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, HotelEntity>): RemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            hotelDatabase.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, HotelEntity>): RemoteKeysEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            hotelDatabase.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, HotelEntity>): RemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                hotelDatabase.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }

    private fun mapper(hotel: List<HotelItem>?): List<HotelEntity> {
        if (hotel != null) {
            return hotel.map {
                HotelEntity(
                    image = it.image,
                    name = it.name,
                    location = it.location,
                    price = it.price,
                    id = it.id,
                    facilities = it.facilities?.toMutableList(),
                    lat = it.lat,
                    lon = it.lon,
                    rating = it.rating,
                    review = it.review
                )
            }
        }
        return emptyList()
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 0
    }

}