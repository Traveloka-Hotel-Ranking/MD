package com.traveloka.hotelranking.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.traveloka.hotelranking.data.local.entity.HotelEntity

@Dao
interface HotelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: List<HotelEntity>)

    @Query("SELECT * FROM hotel")
    fun getAllGame(): PagingSource<Int, HotelEntity>

    @Query("DELETE FROM hotel")
    suspend fun deleteAll()

}