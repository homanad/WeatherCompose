package com.homalab.android.compose.weather.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.homalab.android.compose.weather.domain.entity.*

@Entity
data class WeatherDataEntity(
    @PrimaryKey
    val id: Int,
    val coord: Coord,
    val weather: Weather,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
//    val rain: Map<String, String>,
    val cloud: Cloud,
//    val snow: Map<String, String>,
    val dt: Long,
    val sys: Sys,
    val timeZone: Int,
    val name: String,
    val cod: Int
)