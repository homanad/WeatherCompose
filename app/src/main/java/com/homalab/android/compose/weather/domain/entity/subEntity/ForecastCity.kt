package com.homalab.android.compose.weather.domain.entity.subEntity

data class ForecastCity(
    val id: Int,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Int,
    val timeZone: Int,
    val sunrise: Long,
    val sunset: Long
)