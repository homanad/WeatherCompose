package com.homalab.android.compose.weather.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.homalab.android.compose.weather.data.common.BaseDao
import com.homalab.android.compose.weather.data.db.entity.WeatherDataEntity

@Dao
abstract class WeatherDataDao : BaseDao<WeatherDataEntity> {

    @Query("SELECT * FROM WeatherDataEntity WHERE id = :id")
    abstract fun getWeatherDataById(id: Int): WeatherDataEntity
}