package com.traveloka.hotelranking.data.local.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.traveloka.hotelranking.data.remote.response.Facilities

class DataConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromMutableList(list: List<Facilities>): String {
            val gson = Gson()
            val type = object : TypeToken<List<Facilities>>() {}.type
            return gson.toJson(list, type)
        }

        @TypeConverter
        @JvmStatic
        fun toMutableList(string: String): List<Facilities> {
            val gson = Gson()
            val type = object : TypeToken<List<Facilities>>() {}.type
            return gson.fromJson(string, type)
        }
    }
}