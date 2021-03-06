package com.homalab.android.compose.weather.domain.entity

import com.homalab.android.compose.weather.domain.entity.subEntity.*

data class WeatherData(
    val coord: Coord,
    val weather: List<WeatherItem>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val rain: RainOrSnow?,
    val snow: RainOrSnow?,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timeZone: Int,
    val id: Int,
    val name: String,
    val cod: Int
)