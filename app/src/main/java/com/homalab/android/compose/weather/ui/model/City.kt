package com.homalab.android.compose.weather.ui.model

data class City(
    val id: Int,
    val name: String,
    val state: String,
    val country: String,
    val coord: Coord
)

data class Coord(
    val lat: Double,
    val lng: Double
)