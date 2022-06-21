package com.homalab.android.compose.weather.data.api.service

import com.homalab.android.compose.weather.data.common.DataConstants.OPEN_WEATHER_API_KEY
import com.homalab.android.compose.weather.domain.entity.WeatherData
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    fun getCurrentWeather(
        @Query("appid") appId: String = OPEN_WEATHER_API_KEY,
        @Query("lat") lat: Float,
        @Query("lon") lon: Float
    ): WeatherData
}