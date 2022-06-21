package com.homalab.android.compose.weather.data.mapper

import com.homalab.android.compose.weather.data.db.entity.WeatherDataEntity
import com.homalab.android.compose.weather.domain.entity.WeatherData

fun WeatherDataEntity.toWeatherData() = WeatherData(
    coord,
    weather,
    base,
    main,
    visibility,
    wind,
    cloud,
    dt,
    sys,
    timeZone,
    id,
    name,
    cod
)

fun WeatherData.toWeatherDataEntity() = WeatherDataEntity(
    id,
    coord,
    weather,
    base,
    main,
    visibility,
    wind,
    cloud,
    dt,
    sys,
    timeZone,
    name,
    cod
)