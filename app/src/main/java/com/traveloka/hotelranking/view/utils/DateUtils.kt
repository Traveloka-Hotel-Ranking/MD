package com.traveloka.hotelranking.view.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun formatDateToString(time: Date?, pattern: String?): String? {
        val dateFormat = SimpleDateFormat(pattern)
        return dateFormat.format(time)
    }
}