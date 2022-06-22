package com.homalab.android.compose.weather.domain.usecase

import com.homalab.android.compose.weather.domain.WeatherRepository
import com.homalab.android.compose.weather.domain.common.BaseUseCase
import com.homalab.android.compose.weather.domain.entity.WeatherData

class GetLastWeatherUseCase(
    private val weatherRepository: WeatherRepository
) : BaseUseCase<Int, WeatherData>() {

    override suspend fun create(param: Int): WeatherData {
        return weatherRepository.getLastWeatherData()
    }
}