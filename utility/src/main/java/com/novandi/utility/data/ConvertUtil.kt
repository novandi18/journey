package com.novandi.utility.data

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object ConvertUtil {
    fun convertMillisToDate(milliseconds: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliseconds

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
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
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
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

    fun isDeadlinePassed(value: String): Boolean {
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
        val targetDate = formatter.parse(value)
        val today = Date()

        return targetDate!!.before(today)
    }

    fun getCurrentTimestamp(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(Date())
    }
}