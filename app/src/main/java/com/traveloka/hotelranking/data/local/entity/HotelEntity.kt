package com.traveloka.hotelranking.data.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.traveloka.hotelranking.data.remote.response.Facilities

@Entity(tableName = "hotel")
data class HotelEntity(
    @ColumnInfo(name = "image")
    val image: String = "",

    @ColumnInfo(name = "price")
    val price: String = "",

    @ColumnInfo(name = "review")
    val review: Double = 0.0,

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "rating")
    val rating: Double = 0.0,

    @ColumnInfo(name = "location")
    val location: String = "",

    @ColumnInfo(name = "lon")
    val lon: String = "",

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "lat")
    val lat: String = "",

    @ColumnInfo(name = "facilities")
    val facilities: MutableList<Facilities>? = null,
)
