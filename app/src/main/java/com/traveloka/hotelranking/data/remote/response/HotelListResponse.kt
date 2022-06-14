package com.traveloka.hotelranking.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class HotelListResponse(

	@field:SerializedName("response")
	val response: Response? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null,
) : Parcelable

@Parcelize
data class Response(
	@field:SerializedName("totalItems")
	val totalItems: Int? = null,

	@field:SerializedName("totalPages")
	val totalPages: Int? = null,

	@field:SerializedName("hotel")
	val hotel: List<HotelItem>? = null,

	@field:SerializedName("currentPage")
	val currentPage: Int? = null
): Parcelable

@Parcelize
data class HotelItem(

	@field:SerializedName("image")
	val image: String = "",

	@field:SerializedName("price")
	val price: String = "",

	@field:SerializedName("review")
	val review: Double = 0.0,

	@field:SerializedName("name")
	val name: String = "",

	@field:SerializedName("rating")
	val rating: Double = 0.0,

	@field:SerializedName("location")
	val location: String = "",

	@field:SerializedName("lon")
	val lon: String = "",

	@field:SerializedName("id")
	val id: Int = 0,

	@field:SerializedName("lat")
	val lat: String = "",

	@field:SerializedName("facilities")
	val facilities: List<Facilities>? = null,
) : Parcelable

@Parcelize
data class Facilities(
	@field:SerializedName("id")
	val id: Int = 0,

	@field:SerializedName("capacity")
	val capacity: Int = 0,

	@field:SerializedName("standard_bed")
	val standard_bed: Int = 0,

	@field:SerializedName("medium_bed")
	val medium_bed: Int = 0,

	@field:SerializedName("king_bed")
	val king_bed: Int = 0,

	@field:SerializedName("ac")
	val ac: Boolean,

	@field:SerializedName("wifi")
	val wifi: Boolean,

	@field:SerializedName("breakfast")
	val breakfast: Boolean,

	@field:SerializedName("lunch")
	val lunch: Boolean,

	@field:SerializedName("dinner")
	val dinner: Boolean,

	@field:SerializedName("hotelId")
	val hotelId: Int
) : Parcelable
