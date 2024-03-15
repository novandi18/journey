package com.novandi.core.consts

import com.novandi.core.domain.model.Sector

object Sectors {
    fun getSectors() = listOf(
        Sector(id = 1, sector = "Investment"),
        Sector(id = 2, sector = "Finance"),
        Sector(id = 3, sector = "Stock Market"),
        Sector(id = 4, sector = "Technology"),
        Sector(id = 5, sector = "Health care"),
        Sector(id = 6, sector = "Economics"),
        Sector(id = 7, sector = "Construction"),
        Sector(id = 8, sector = "Mining"),
        Sector(id = 9, sector = "Financial services"),
        Sector(id = 10, sector = "Agriculture"),
        Sector(id = 11, sector = "Aerospace"),
        Sector(id = 12, sector = "Infrastructure"),
        Sector(id = 13, sector = "Logistics"),
        Sector(id = 14, sector = "Advertising"),
        Sector(id = 15, sector = "Retail"),
        Sector(id = 16, sector = "Trade"),
        Sector(id = 17, sector = "Production"),
        Sector(id = 18, sector = "Entertainment"),
        Sector(id = 19, sector = "Communcation"),
        Sector(id = 20, sector = "Computers and information technology"),
        Sector(id = 21, sector = "Real Estate"),
        Sector(id = 22, sector = "Stock Exchange"),
        Sector(id = 23, sector = "Energy industry"),
        Sector(id = 24, sector = "Energy"),
        Sector(id = 25, sector = "Strategy"),
        Sector(id = 26, sector = "Materials science"),
        Sector(id = 27, sector = "Sport"),
        Sector(id = 28, sector = "Design"),
        Sector(id = 29, sector = "Forestry"),
        Sector(id = 30, sector = "Fishery"),
        Sector(id = 31, sector = "Transport"),
        Sector(id = 32, sector = "Education"),
        Sector(id = 33, sector = "Rail transport"),
        Sector(id = 34, sector = "Operations management"),
        Sector(id = 35, sector = "Telecommunications"),
        Sector(id = 36, sector = "Food industry"),
        Sector(id = 37, sector = "Marketing"),
        Sector(id = 38, sector = "Research"),
        Sector(id = 39, sector = "Information and communcations technology"),
        Sector(id = 40, sector = "Capital market"),
        Sector(id = 41, sector = "Law"),
        Sector(id = 42, sector = "Media"),
        Sector(id = 43, sector = "Tire"),
        Sector(id = 43, sector = "Medicine"),
        Sector(id = 44, sector = "Electronics"),
        Sector(id = 45, sector = "Internet")
    )

    fun getSectorById(id: Int) = getSectors().filter { sector -> sector.id == id }[0]
}