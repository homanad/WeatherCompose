package com.homalab.android.compose.weather.ui.mapper

import com.algolia.search.model.ObjectID
import com.homalab.android.compose.weather.domain.entity.WeatherData
import com.homalab.android.compose.weather.ui.model.CityRecord

fun WeatherData.toCity() = CityRecord(
    id = id,
    name = name,
    state = "state",
    country = "country",
    coord = coord,
    objectID = ObjectID("objectID")
)