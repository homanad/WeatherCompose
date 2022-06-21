package com.homalab.android.compose.weather.domain.usecase

import com.homalab.android.compose.weather.domain.WeatherRepository
import com.homalab.android.compose.weather.domain.common.BaseUseCase
import com.homalab.android.compose.weather.domain.entity.WeatherData

class GetCurrentWeatherUseCase(
    private val weatherRepository: WeatherRepository
) : BaseUseCase<Unit, WeatherData>() {

    override suspend fun create(param: Unit): WeatherData {
        return weatherRepository.getCurrentWeatherData()
    }
}