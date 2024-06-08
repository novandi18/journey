package com.novandi.utility.data

fun getFilenameFromUrl(url: String): String {
    val lastSlashIndex = url.lastIndexOf('/')
    if (lastSlashIndex >= 0) {
        return url.substring(lastSlashIndex + 1)
    }
    return ""
}