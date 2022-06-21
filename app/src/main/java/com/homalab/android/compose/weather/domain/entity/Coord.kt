package com.homalab.android.compose.weather.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class Coord(
    val lat: Double,
    val lon: Double
)