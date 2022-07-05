package com.homalab.android.compose.weather.data.datasource

import com.homalab.android.compose.weather.domain.entity.City

interface CityDataSource {
    suspend fun search(keyword: String): List<City>
}