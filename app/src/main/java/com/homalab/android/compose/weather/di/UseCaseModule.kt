package com.homalab.android.compose.weather.di

import com.homalab.android.compose.weather.domain.WeatherRepository
import com.homalab.android.compose.weather.domain.usecase.GetCurrentWeatherUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetCurrentWeatherUseCase(weatherRepository: WeatherRepository) =
        GetCurrentWeatherUseCase(weatherRepository)
}