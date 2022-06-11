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
	val hotel: List<HotelItem?>? = null,

	@field:SerializedName("currentPage")
	val currentPage: Int? = null
): Parcelable

@Parcelize
data class HotelItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("review")
	val review: Double? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("rating")
	val rating: Double? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("lon")
	val lon: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("lat")
	val lat: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
) : Parcelable
