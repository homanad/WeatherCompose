package com.homalab.android.compose.weather.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.homalab.android.compose.weather.data.common.BaseDao
import com.homalab.android.compose.weather.data.db.entity.ForecastDataEntity

@Dao
abstract class ForecastDataDao : BaseDao<ForecastDataEntity> {

    @Query("SELECT * FROM ForecastDataEntity WHERE lat = :lat AND lon = :lon")
    abstract fun getForecastData(lat: Double, lon: Double): ForecastDataEntity
}