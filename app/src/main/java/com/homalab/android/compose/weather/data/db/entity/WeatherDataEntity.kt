package com.homalab.android.compose.weather.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.homalab.android.compose.weather.domain.entity.subEntity.*

@Entity
data class WeatherDataEntity(
    @PrimaryKey
    val id: Int,
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
    val name: String,
    val cod: Int,
    val timestamp: Long
)