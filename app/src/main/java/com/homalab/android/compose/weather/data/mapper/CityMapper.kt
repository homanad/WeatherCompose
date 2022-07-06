package com.homalab.android.compose.weather.data.mapper

import com.homalab.android.compose.weather.data.db.entity.WeatherDataEntity
import com.homalab.android.compose.weather.data.model.CityRecord
import com.homalab.android.compose.weather.domain.entity.City
import com.homalab.android.compose.weather.domain.entity.WeatherData

fun WeatherData.toCity() = City(
    id = id,
    name = name,
    state = "state",
    country = "country",
    coord = coord
)

fun WeatherDataEntity.toCity() = City(
    id = id,
    name = name,
    state = "state",
    country = "country",
    coord = coord
)

fun CityRecord.toCity() = City(
    id = id,
    name = name,
    state = state,
    country = country,
    coord = coord
)