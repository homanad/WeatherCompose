package com.homalab.android.compose.weather.di

import android.content.Context
import androidx.room.Room
import com.homalab.android.compose.weather.data.api.ApiFactory
import com.homalab.android.compose.weather.data.api.service.WeatherService
import com.homalab.android.compose.weather.data.db.WeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideWeatherDatabase(@ApplicationContext context: Context): WeatherDatabase {
        return Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            WeatherDatabase::class.java.simpleName
        ).build()
    }

    @Provides
    fun provideWeatherDataDao(weatherDatabase: WeatherDatabase) = weatherDatabase.weatherDataDao

    @Provides
    fun provideWeatherService() = ApiFactory.createRetrofit<WeatherService>()
}