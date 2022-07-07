package com.homalab.android.compose.weather.data.mapper

import com.homalab.android.compose.weather.data.db.entity.ForecastDataEntity
import com.homalab.android.compose.weather.domain.entity.ForecastData

fun ForecastDataEntity.toForecastData() = ForecastData(
    cnt = list.size,
    list = list,
    city = city
)

fun ForecastData.toForecastDataEntity() = ForecastDataEntity(
    lat = city.coord.lat,
    lon = city.coord.lon,
    list = list,
    city = city
)