package com.homalab.android.compose.weather.presentation.ui.detail

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
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
import com.homalab.android.compose.weather.domain.entity.subEntity.Main
import com.homalab.android.compose.weather.presentation.components.*
import com.homalab.android.compose.weather.presentation.mapper.ForecastDayItem
import com.homalab.android.compose.weather.util.*
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun DetailBackLayerDisplay(
    forecastDayItem: ForecastDayItem?,
    timeZone: Int,
    modifier: Modifier = Modifier,
) {
    if (forecastDayItem != null) {
        DetailBackLayerInfo(forecastDayItem = forecastDayItem, timeZone, modifier)
    } else {
        MessageText(
            text = stringResource(id = R.string.network_unavailable)
        )
    }
}

@Composable
fun DetailBackLayerInfo(
    forecastDayItem: ForecastDayItem,
    timeZone: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        TemperatureChart(
            title = stringResource(id = R.string.temperature),
            data = forecastDayItem,
            timeZone
        )

        DefaultSpacer()

        RainChart(title = stringResource(id = R.string.rain), data = forecastDayItem, timeZone)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TemperatureChart(
    title: String,
    data: ForecastDayItem,
    timeZone: Int
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Dimension2, end = Dimension2, bottom = Dimension2),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title)
            SmallSpacer()
            HorizontalDivider(modifier = Modifier.padding(start = Dimension1))
        }

        val minData = MultipleChartData(
            dotColor = Color.Green,
            lineColor = Color.Green,
            values = data.list.map {
                MultipleChartValue(TimeFormatter.formatChartTime(it.dt, timeZone), it.main.temp_min)
            }
        )

        val normalData = MultipleChartData(
            dotColor = Color.Yellow,
            lineColor = Color.Yellow,
            values = data.list.map {
                MultipleChartValue(TimeFormatter.formatChartTime(it.dt, timeZone), it.main.temp)
            }
        )

        val maxData = MultipleChartData(
            dotColor = Color.Red,
            lineColor = Color.Red,
            values = data.list.map {
                MultipleChartValue(TimeFormatter.formatChartTime(it.dt, timeZone), it.main.temp_max)
            }
        )
        val multipleChartData = listOf(minData, maxData, normalData)

        AnimatedContent(
            targetState = multipleChartData,
            transitionSpec = {
                expandHorizontally(
                    animationSpec = tween(DURATION_DOUBLE_LONG, delayMillis = DURATION_SMALL),
                    expandFrom = Alignment.Start
                ) with shrinkHorizontally(
                    animationSpec = tween(DURATION_SMALL),
                    shrinkTowards = Alignment.Start
                )
            }
        ) { chartData ->
            MultipleLinesChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimension4),
                chartData = chartData,
                verticalAxisValues = generateMinMaxRange(
                    minData.values.minOf { it.value },
                    maxData.values.maxOf { it.value }
                ),
                verticalAxisLabelTransform = {
                    String.format(
                        Constants.C_DEGREE_PATTERN,
                        it.toInt()
                    )
                }
            )
        }
    }
}

@Composable
fun RainChart(
    title: String,
    data: ForecastDayItem,
    timeZone: Int
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Dimension2, end = Dimension2, bottom = Dimension2),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title)
            SmallSpacer()
            HorizontalDivider(modifier = Modifier.padding(start = Dimension1))
        }
//    val barChartData = getTestBarChartData()
        val barChartData = data.list.map {
            BarChartData(
                Color.Cyan,
                barValue = it.rain?.`3h` ?: 0f,
                TimeFormatter.formatChartTime(it.dt, timeZone)
            )
        }

        var verticalAxisValues = generateMinMaxRangeForBarChart(
            barChartData.minOf { it.barValue },
            barChartData.maxOf { it.barValue })

        if (verticalAxisValues.isEmpty()) verticalAxisValues = listOf(0f, 1f)

        BarChart(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimension4),
            chartData = barChartData,
            verticalAxisValues = verticalAxisValues,
            verticalAxisLabelTransform = { it.toString() }
        )
    }
}

fun generateMinMaxRange(min: Float, max: Float): List<Float> {
    val minValue = floor(min).toInt()
    val maxValue = ceil(max).toInt()

    val delta = maxValue - minValue
    val step = ceil(delta.toFloat() / MAX_HORIZONTAL_LINE).toInt()

    val list = mutableListOf<Float>()
    var value = minValue - step
    while (value < maxValue) {
        value += step
        list.add(value.toFloat())
    }
//    for (i in minValue..maxValue step step) {
//        list.add(i.toFloat()) //TODO fix step out of value
//    }
    return list
}

fun generateMinMaxRangeForBarChart(min: Float, max: Float): List<Float> {
    val minValue = floor(min).toInt()
    val maxValue = ceil(max).toInt()

    val delta = maxValue - minValue
    val step = ceil(delta.toFloat() / MAX_HORIZONTAL_LINE).toInt()

    val list = mutableListOf<Float>()
    var value = minValue - step
    while (value < maxValue) {
        value += step
        list.add(value.toFloat())
    }
    return list
}

private const val MAX_HORIZONTAL_LINE = 5

fun getTestChartData(): List<Main> {
    return listOf(
        Main(
            temp_min = 13f,
            temp = 15f,
            temp_max = 17f,
            feels_like = 1f,
            pressure = 1f,
            humidity = 1f
        ),
        Main(
            temp_min = 14f,
            temp = 16f,
            temp_max = 18f,
            feels_like = 1f,
            pressure = 1f,
            humidity = 1f
        ),
        Main(
            temp_min = 15f,
            temp = 17f,
            temp_max = 19f,
            feels_like = 1f,
            pressure = 1f,
            humidity = 1f
        ),
        Main(
            temp_min = 16f,
            temp = 18f,
            temp_max = 20f,
            feels_like = 1f,
            pressure = 1f,
            humidity = 1f
        ),
    )
}

fun getTestBarChartData(): List<BarChartData> {
    return listOf(
        BarChartData(Color.Blue, 21f, "Blue"),
        BarChartData(Color.Black, 25f, "Black"),
        BarChartData(Color.Gray, 30f, "Gray"),
        BarChartData(Color.Yellow, 30f, "Yellow"),
    )
}