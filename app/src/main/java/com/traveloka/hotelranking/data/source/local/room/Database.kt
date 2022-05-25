package com.traveloka.hotelranking.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.traveloka.hotelranking.data.source.local.entity.HotelEntity
import com.traveloka.hotelranking.data.source.local.entity.RemoteKeysEntity

@Database(
    entities = [HotelEntity::class, RemoteKeysEntity::class],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {

    abstract fun hotelDao(): HotelDao
    abstract fun remoteKeysDao(): RemoteKeysDao

}