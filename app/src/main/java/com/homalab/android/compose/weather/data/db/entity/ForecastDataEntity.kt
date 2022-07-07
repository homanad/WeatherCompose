package com.homalab.android.compose.weather.data.db.entity

import androidx.room.Entity
import com.homalab.android.compose.weather.domain.entity.subEntity.ForecastCity
import com.homalab.android.compose.weather.domain.entity.subEntity.ForecastItem

@Entity(primaryKeys = ["lon", "lat"])
data class ForecastDataEntity(
    val lon: Double,
    val lat: Double,
    val list: List<ForecastItem>,
    val city: ForecastCity
)