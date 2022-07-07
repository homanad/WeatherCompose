package com.homalab.android.compose.weather.data.db.dao

import androidx.room.Dao
import com.homalab.android.compose.weather.data.common.BaseDao
import com.homalab.android.compose.weather.data.db.entity.ForecastDataEntity

@Dao
abstract class ForecastDataDao : BaseDao<ForecastDataEntity> {

}