package com.homalab.android.compose.weather.di

import com.homalab.android.compose.weather.data.datasource.WeatherDataSource
import com.homalab.android.compose.weather.data.datasource.WeatherLocalDataSourceImpl
import com.homalab.android.compose.weather.data.datasource.WeatherRemoteDataSourceImpl
import com.homalab.android.compose.weather.data.repository.WeatherRepositoryImpl
import com.homalab.android.compose.weather.domain.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @RemoteDataSource
    @Binds
    fun bindWeatherRemoteDataSource(remoteDataSourceImpl: WeatherRemoteDataSourceImpl): WeatherDataSource

    @LocalDataSource
    @Binds
    fun bindWeatherLocalDataSource(localDataSourceImpl: WeatherLocalDataSourceImpl): WeatherDataSource

    @Binds
    fun bindWeatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository
}