package com.homalab.android.compose.weather.domain.common

abstract class BaseUseCase<Param, Result> {

    abstract suspend fun create(param: Param): Result

    suspend operator fun invoke(param: Param) = create(param)
}