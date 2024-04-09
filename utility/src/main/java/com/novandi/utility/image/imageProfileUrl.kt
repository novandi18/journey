package com.novandi.utility.image

import com.novandi.utility.consts.NetworkUrls

fun String.imageProfileUrl() = if (this != "https://storage.googleapis.com/journey-bangkit/company.png"
    && this != "https://storage.googleapis.com/journey-bangkit/profile.png")
    "${NetworkUrls.JOURNEY}users/image/$this" else this