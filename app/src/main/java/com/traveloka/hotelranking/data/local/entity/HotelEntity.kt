package com.traveloka.hotelranking.data.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hotel")
data class HotelEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id_hotel")
    var id_user: Int,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "location")
    var no_telp: String
)
