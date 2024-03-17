package com.novandi.utility.image

fun String.imageProfileUrl() = if (this != "https://storage.googleapis.com/journey-bangkit/company.png"
    && this != "https://storage.googleapis.com/journey-bangkit/profile.png")
    "http://10.0.2.2:3000/api/users/image/$this" else this