package com.homalab.android.compose.weather.domain.usecase

import com.homalab.android.compose.weather.domain.repository.WeatherRepository
import com.homalab.android.compose.weather.domain.common.BaseUseCase
import com.homalab.android.compose.weather.domain.entity.City

class GetSavedCitiesUseCase(
    private val weatherRepository: WeatherRepository
) : BaseUseCase<Int, List<City>>() {

    override suspend fun create(param: Int): List<City> {
        return weatherRepository.getSavedCities()
    }
}