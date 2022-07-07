package com.homalab.android.compose.weather.domain.usecase

import com.homalab.android.compose.weather.domain.common.BaseUseCase
import com.homalab.android.compose.weather.domain.entity.ForecastData
import com.homalab.android.compose.weather.domain.repository.WeatherRepository

class GetForecastDataUseCase(
    private val weatherRepository: WeatherRepository
) : BaseUseCase<GetForecastDataUseCase.GetForecastDataParam, ForecastData>() {

    override suspend fun create(param: GetForecastDataParam): ForecastData {
        return weatherRepository.getForecastData()
    }

    data class GetForecastDataParam(
        val lat: Double,
        val lon: Double
    )
}