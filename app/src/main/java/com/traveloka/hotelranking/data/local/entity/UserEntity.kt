package com.traveloka.hotelranking.data.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity (
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id_user")
    var id_user: Int,

    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "no_telp")
    var no_telp: String
)