package com.homalab.android.compose.weather.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.homalab.android.compose.weather.R
import com.homalab.android.compose.weather.util.isInRange

@Composable
fun WeatherConditionLoader(conditionId: Int, modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(getWeatherConditionRawRes(conditionId))
    )

    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = modifier
    )
}

fun getWeatherConditionRawRes(conditionId: Int): Int {
    return when {
        conditionId.isInRange(200, 232) -> {
            R.raw.weather_thunderstorm
        }
        conditionId.isInRange(300, 321) -> {
            R.raw.weather_dizzle
        }
        conditionId.isInRange(500, 531) -> {
            R.raw.weather_rain
        }
        conditionId.isInRange(600, 622) -> {
            R.raw.weather_snow
        }
        conditionId.isInRange(701, 781) -> {
            R.raw.weather_atmosphere
        }
        conditionId == 800 -> {
            R.raw.weather_clear
        }
        conditionId.isInRange(801, 804) -> {
            R.raw.weather_clouds
        }
        else -> R.raw.weather_atmosphere
    }
}