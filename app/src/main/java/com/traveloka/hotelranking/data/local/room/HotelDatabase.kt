package com.traveloka.hotelranking.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.traveloka.hotelranking.data.local.entity.HotelEntity
import com.traveloka.hotelranking.data.local.entity.RemoteKeysEntity

@Database(
    entities = [HotelEntity::class, RemoteKeysEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DataConverter::class)
abstract class HotelDatabase : RoomDatabase() {

    abstract fun hotelDao(): HotelDao
    abstract fun remoteKeysDao(): RemoteKeysDao

}