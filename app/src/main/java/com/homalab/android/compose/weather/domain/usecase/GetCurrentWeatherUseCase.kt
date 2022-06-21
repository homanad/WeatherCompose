package com.homalab.android.compose.weather.domain.usecase

import com.homalab.android.compose.weather.domain.WeatherRepository
import com.homalab.android.compose.weather.domain.common.BaseUseCase
import com.homalab.android.compose.weather.domain.entity.WeatherData

class GetCurrentWeatherUseCase(
    private val weatherRepository: WeatherRepository
) : BaseUseCase<GetCurrentWeatherUseCase.GetCurrentWeatherParam, WeatherData>() {

    override suspend fun create(param: GetCurrentWeatherParam): WeatherData {
        return weatherRepository.getCurrentWeatherData(param.id, param.lat, param.lon)
    }

    data class GetCurrentWeatherParam(
        val id: Int,
        val lat: Float,
        val lon: Float
    )
}