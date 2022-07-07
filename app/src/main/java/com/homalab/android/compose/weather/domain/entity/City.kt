package com.homalab.android.compose.weather.domain.entity

import com.homalab.android.compose.weather.domain.entity.subEntity.Coord

data class City(
    val id: Int,
    val name: String,
    val state: String,
    val country: String,
    val coord: Coord,
)