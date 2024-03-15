package com.novandi.core.consts

import com.novandi.core.domain.model.Province

object Provinces {
    fun getProvinces() = listOf(
        Province(id = "11", province = "Aceh"),
        Province(id = "12", province = "Sumatera Utara"),
        Province(id = "13", province = "Sumatera Barat"),
        Province(id = "14", province = "Riau"),
        Province(id = "15", province = "Jambi"),
        Province(id = "16", province = "Sumatera Selatan"),
        Province(id = "17", province = "Bengkulu"),
        Province(id = "18", province = "Lampung"),
        Province(id = "19", province = "Kepulauan Bangka Belitung"),
        Province(id = "21", province = "Kepulauan Riau"),
        Province(id = "31", province = "Dki Jakarta"),
        Province(id = "32", province = "Jawa Barat"),
        Province(id = "33", province = "Jawa Tengah"),
        Province(id = "34", province = "Di Yogyakarta"),
        Province(id = "35", province = "Jawa Timur"),
        Province(id = "36", province = "Banten"),
        Province(id = "51", province = "Bali"),
        Province(id = "52", province = "Nusa Tenggara Barat"),
        Province(id = "53", province = "Nusa Tenggara Timur"),
        Province(id = "61", province = "Kalimantan Barat"),
        Province(id = "62", province = "Kalimantan Tengah"),
        Province(id = "63", province = "Kalimantan Selatan"),
        Province(id = "64", province = "Kalimantan Timur"),
        Province(id = "65", province = "Kalimantan Utara"),
        Province(id = "71", province = "Sulawesi Utara"),
        Province(id = "72", province = "Sulawesi Tengah"),
        Province(id = "73", province = "Sulawesi Selatan"),
        Province(id = "74", province = "Sulawesi Tenggara"),
        Province(id = "75", province = "Gorontalo"),
        Province(id = "76", province = "Sulawesi Barat"),
        Province(id = "81", province = "Maluku"),
        Province(id = "82", province = "Maluku Utara"),
        Province(id = "91", province = "Papua Barat"),
        Province(id = "94", province = "Papua")
    )

    fun getProvinceById(id: String) = getProvinces().filter { province -> province.id == id }[0]
}