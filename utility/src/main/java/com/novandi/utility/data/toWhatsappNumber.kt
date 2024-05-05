package com.novandi.utility.data

fun String.toWhatsappNumber(): String {
    val phoneCode = this.take(3)
    return if (phoneCode !== "628") "62" + this.substring(1) else this
}