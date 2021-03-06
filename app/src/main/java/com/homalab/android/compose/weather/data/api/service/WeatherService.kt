package com.homalab.android.compose.weather.data.api.service

import com.homalab.android.compose.weather.data.common.DataConstants.OPEN_WEATHER_API_KEY
import com.homalab.android.compose.weather.data.common.DataConstants.OPEN_WEATHER_DEFAULT_UNITS
import com.homalab.android.compose.weather.domain.entity.ForecastData
import com.homalab.android.compose.weather.domain.entity.WeatherData
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String = OPEN_WEATHER_API_KEY,
        @Query("units") units: String = OPEN_WEATHER_DEFAULT_UNITS
    ): WeatherData

    @GET("forecast")
    suspend fun getForecastData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String = OPEN_WEATHER_API_KEY,
        @Query("units") units: String = OPEN_WEATHER_DEFAULT_UNITS
    ): ForecastData
}