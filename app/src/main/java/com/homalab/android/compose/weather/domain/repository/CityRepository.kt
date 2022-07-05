package com.homalab.android.compose.weather.domain.repository

import com.homalab.android.compose.weather.domain.entity.City

interface CityRepository {
    suspend fun search(keyword: String): List<City>
}