package com.homalab.android.compose.weather.domain.usecase

import com.homalab.android.compose.weather.domain.repository.WeatherRepository
import com.homalab.android.compose.weather.domain.common.BaseUseCase
import com.homalab.android.compose.weather.domain.entity.WeatherData

class GetSavedWeatherUseCase(
    private val weatherRepository: WeatherRepository
) : BaseUseCase<Int, List<WeatherData>>() {

    override suspend fun create(param: Int): List<WeatherData> {
        return weatherRepository.getSavedWeathers()
    }
}