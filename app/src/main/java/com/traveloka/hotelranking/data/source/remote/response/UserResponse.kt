package com.traveloka.hotelranking.data.source.remote.response

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class UserResponse(

    @field:SerializedName("id_user")
    var id_user: Int,

    @field:SerializedName("email")
    var email: String,

    @field:SerializedName("name")
    var name: String,

    @field:SerializedName("no_telp")
    var no_telp: String

)
