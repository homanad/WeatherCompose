package com.homalab.android.compose.weather.presentation.ui.detail

import androidx.compose.ui.graphics.Color
import com.homalab.android.compose.weather.domain.entity.subEntity.Main
import com.homalab.android.compose.weather.presentation.components.BarChartData

//fun getTestChartData(): List<Main> {
//    return listOf(
//        Main(
//            temp_min = 13f,
//            temp = 15f,
//            temp_max = 17f,
//            feels_like = 1f,
//            pressure = 1f,
//            humidity = 1f
//        ),
//        Main(
//            temp_min = 14f,
//            temp = 16f,
//            temp_max = 18f,
//            feels_like = 1f,
//            pressure = 1f,
//            humidity = 1f
//        ),
//        Main(
//            temp_min = 15f,
//            temp = 17f,
//            temp_max = 19f,
//            feels_like = 1f,
//            pressure = 1f,
//            humidity = 1f
//        ),
//        Main(
//            temp_min = 16f,
//            temp = 18f,
//            temp_max = 20f,
//            feels_like = 1f,
//            pressure = 1f,
//            humidity = 1f
//        ),
//    )
//}

fun getTestBarChartData(): List<BarChartData> {
    return listOf(
        BarChartData(Color.Blue, 21f, "Blue"),
        BarChartData(Color.Black, 25f, "Black"),
        BarChartData(Color.Gray, 30f, "Gray"),
        BarChartData(Color.Yellow, 30f, "Yellow"),
    )
}