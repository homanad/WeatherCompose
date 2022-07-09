package com.homalab.android.compose.weather.presentation.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.homalab.android.compose.weather.R
import com.homalab.android.compose.weather.presentation.components.HorizontalDivider
import com.homalab.android.compose.weather.presentation.components.LineChart
import com.homalab.android.compose.weather.presentation.components.MessageText
import com.homalab.android.compose.weather.presentation.components.SmallSpacer
import com.homalab.android.compose.weather.presentation.mapper.ForecastDayData
import com.homalab.android.compose.weather.presentation.mapper.ForecastDayItem
import com.homalab.android.compose.weather.util.Dimension1
import com.homalab.android.compose.weather.util.Dimension2
import kotlin.math.round
import kotlin.math.roundToInt

@Composable
fun DetailDisplay(
    forecastDayData: ForecastDayData?,
    modifier: Modifier = Modifier,
) {
    if (forecastDayData != null) {
        DetailInfo(forecastDayData = forecastDayData, modifier)
    } else {
        MessageText(
            text = stringResource(id = R.string.network_unavailable)
        )
    }
}

@Composable
fun DetailInfo(forecastDayData: ForecastDayData, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        DataChart(title = "Temp", data = forecastDayData.items[1])
    }
}

@Composable
fun DataChart(
    title: String,
    data: ForecastDayItem
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Dimension2, end = Dimension2),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title)
            SmallSpacer()
            HorizontalDivider(modifier = Modifier.padding(start = Dimension1))
        }

        val chartData = data.list.map {
            it.main
        }
//        val verticalAxis = chartData.map { it.temp }.toMutableList()
//
//        verticalAxis.add(0f)

        println("------test: ${9.8f.roundToTen()}")
        println("------test: ${(-9.8f).roundToTen()}")

        LineChart(
            chartData = chartData,
            lineValues = { it.temp },
            verticalAxisValues = generateMinMaxRange(chartData.minOf { it.temp }, chartData.maxOf { it.temp }),
            strokeColor = Color.Black,
            dotColor = Color.Red
        )
    }
}

fun generateMinMaxRange(min: Float, max: Float): List<Float> {
    val minValue = min.roundToTen()
    val maxValue = max.roundToTen()

    val list = mutableListOf<Float>()
    for (i in minValue..maxValue step 10) {
        list.add(i.toFloat())
    }
    return list
}

fun Float.roundToTen(): Int {
    return (round(this / 10) * 10).roundToInt()
}