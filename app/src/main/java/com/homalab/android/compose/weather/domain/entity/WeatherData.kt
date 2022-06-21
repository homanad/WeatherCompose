package com.homalab.android.compose.weather.domain.entity

data class WeatherData(
    val coord: Coord,
    val weather: List<WeatherItem>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
//    val rain: Map<String, String>,
//    val cloud: Cloud,
//    val snow: Map<String, String>,
    val dt: Long,
    val sys: Sys,
    val timeZone: Int,
    val id: Int,
    val name: String,
    val cod: Int
)