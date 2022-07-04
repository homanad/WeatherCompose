package com.homalab.android.compose.weather.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.homalab.android.compose.weather.data.common.BaseDao
import com.homalab.android.compose.weather.data.common.DataConstants
import com.homalab.android.compose.weather.data.db.entity.WeatherDataEntity

@Dao
abstract class WeatherDataDao : BaseDao<WeatherDataEntity> {

    @Query("SELECT * FROM WeatherDataEntity WHERE id = :id")
    abstract fun getWeatherDataById(id: Int): WeatherDataEntity

    @Query("SELECT * FROM WeatherDataEntity ORDER BY timestamp DESC LIMIT 1")
    abstract fun getLastWeatherData(): WeatherDataEntity

    @Query("SELECT * FROM WeatherDataEntity ORDER BY timestamp ASC LIMIT 1")
    abstract fun getFirstWeatherData(): WeatherDataEntity

    @Query("SELECT COUNT(id) FROM WeatherDataEntity")
    abstract fun count(): Int

    @Query("SELECT * FROM WeatherDataEntity ORDER BY timestamp DESC")
    abstract fun getWeathers(): List<WeatherDataEntity>

    @Transaction
    open fun removeLastIfNeededAndSaveNew(weatherDataEntity: WeatherDataEntity): Long {
        val id = insert(weatherDataEntity)
        if (count() > DataConstants.MAXIMUM_CACHED_WEATHER) {
            delete(getFirstWeatherData())
        }
        return id
    }
}