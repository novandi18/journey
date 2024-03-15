package com.novandi.utility.data

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun dateFormatter(value: String, withTime: Boolean = false): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val date = dateFormat.parse(value)
    val pattern = "dd MMM yyyy" + if (withTime) ", HH:mm" else ""
    val indonesianFormat = SimpleDateFormat(pattern, Locale("id", "ID"))
    return indonesianFormat.format(date)
}