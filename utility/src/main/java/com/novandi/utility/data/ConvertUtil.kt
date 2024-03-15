package com.novandi.utility.data

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object ConvertUtil {
    fun convertMillisToDate(milliseconds: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliseconds

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 0)

        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("id", "ID"))
        return formatter.format(calendar.time)
    }

    fun convertDateToMillis(value: String): Long {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("id", "ID"))
        val date = format.parse(value)
        return date.time
    }

    fun convertDateValueToActualFormat(value: String): String {
        val originalFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        val originalDate = originalFormat.parse(value)

        val calendar = Calendar.getInstance().apply {
            time = originalDate
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }

        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return outputFormat.format(calendar.time)
    }

    fun convertDateTimeToDate(value: String): String {
        val inputFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormatter =
            SimpleDateFormat("d MMMM yyyy", Locale("id")) // "id" for Indonesian locale
        val date = inputFormatter.parse(value)
        return outputFormatter.format(date)
    }
}