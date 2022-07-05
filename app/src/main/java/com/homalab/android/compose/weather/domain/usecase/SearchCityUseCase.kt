package com.homalab.android.compose.weather.domain.usecase

import com.homalab.android.compose.weather.domain.common.BaseUseCase
import com.homalab.android.compose.weather.domain.entity.City
import com.homalab.android.compose.weather.domain.repository.CityRepository

class SearchCityUseCase(
    private val cityRepository: CityRepository
) : BaseUseCase<String, List<City>>() {

    override suspend fun create(param: String): List<City> {
        return cityRepository.search(param)
    }
}