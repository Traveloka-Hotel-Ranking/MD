package com.traveloka.hotelranking.model.param

class HotelListParam(
    var page : Int = 0,
    var perPage: Int = 10,
    val name : String? = "",
) {
    fun toClean() : HashMap<String, Any?>{
        val query = HashMap<String, Any?>()
        query["page"] = page
        query["size"] = perPage
        if (name?.isNotEmpty() == true) query["name"] = name
        return query
    }
}