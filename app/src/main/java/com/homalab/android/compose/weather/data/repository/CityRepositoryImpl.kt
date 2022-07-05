package com.homalab.android.compose.weather.data.repository

import com.homalab.android.compose.weather.data.datasource.CityDataSource
import com.homalab.android.compose.weather.domain.entity.City
import com.homalab.android.compose.weather.domain.repository.CityRepository
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val cityDataSource: CityDataSource
) : CityRepository {

    override suspend fun search(keyword: String): List<City> {
        return cityDataSource.search(keyword)
    }
}