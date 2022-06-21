package com.homalab.android.compose.weather.data.common

import androidx.room.*

@Dao
interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(objs: List<T>)

    @Delete
    fun delete(obj: T): Int

    @Update
    fun update(obj: T): Int
}