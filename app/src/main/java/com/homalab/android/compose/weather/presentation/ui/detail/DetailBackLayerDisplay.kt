package com.homalab.android.compose.weather.presentation.ui.detail

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.homalab.android.compose.weather.R
import com.homalab.android.compose.weather.presentation.components.*
import com.homalab.android.compose.weather.presentation.mapper.ForecastDayItem
import com.homalab.android.compose.weather.presentation.theme.RainColor
import com.homalab.android.compose.weather.presentation.theme.TemperatureMaxColor
import com.homalab.android.compose.weather.presentation.theme.TemperatureMinColor
import com.homalab.android.compose.weather.presentation.theme.TemperatureNormalColor
import com.homalab.android.compose.weather.util.*
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun DetailBackLayerDisplay(
    forecastDayItem: ForecastDayItem?,
    modifier: Modifier = Modifier,
) {
    DefaultVerticalSpacer()
    if (forecastDayItem != null) {
        DetailBackLayerInfo(forecastDayItem = forecastDayItem, modifier)
    } else {
        MessageText(
            text = stringResource(id = R.string.network_unavailable)
        )
    }
}

@Composable
fun DetailBackLayerInfo(
    forecastDayItem: ForecastDayItem,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        TemperatureChart(data = forecastDayItem)

        RainChart(data = forecastDayItem)
    }
}

@Composable
fun TemperatureChart(
    data: ForecastDayItem,
) {
    val minData = MultipleChartData(
        dotColor = TemperatureMinColor,
        lineColor = TemperatureMinColor,
        values = data.list.map {
            MultipleChartValue(
                TimeFormatter.formatChartTime(it.dt, data.timeZone),
                it.main.temp_min
            )
        },
        label = stringResource(id = R.string.min),
        dotRatio = 2.5f
    )

    val normalData = MultipleChartData(
        dotColor = TemperatureNormalColor,
        lineColor = TemperatureNormalColor,
        values = data.list.map {
            MultipleChartValue(
                TimeFormatter.formatChartTime(it.dt, data.timeZone),
                it.main.temp
            )
        },
        label = stringResource(id = R.string.normal),
        dotRatio = 2f
    )

    val maxData = MultipleChartData(
        dotColor = TemperatureMaxColor,
        lineColor = TemperatureMaxColor,
        values = data.list.map {
            MultipleChartValue(
                TimeFormatter.formatChartTime(it.dt, data.timeZone),
                it.main.temp_max
            )
        },
        label = stringResource(id = R.string.max),
        dotRatio = 1.5f
    )
    val multipleChartData = listOf(minData, normalData, maxData)

    TitledChart(title = stringResource(id = R.string.temperature), modifier = titledChartModifier) {
        ChartAnimatedContent(
            targetState = multipleChartData,
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
                    formatCDegree(it)
                }
            )
        }
    }
}

@Composable
fun RainChart(
    data: ForecastDayItem,
) {
//    val barChartData = getTestBarChartData()
    val barChartData = data.list.map {
        BarChartData(
            RainColor,
            barValue = it.rain?.`3h` ?: 0f,
            TimeFormatter.formatChartTime(it.dt, data.timeZone)
        )
    }

    var verticalAxisValues = generateMinMaxRange(
        barChartData.minOf { it.barValue },
        barChartData.maxOf { it.barValue })

    if (verticalAxisValues.isEmpty()) verticalAxisValues = listOf(0f, 1f)

    TitledChart(title = stringResource(id = R.string.rain), modifier = titledChartModifier) {
        ChartAnimatedContent(targetState = barChartData) { chartData ->
            BarChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimension4),
                chartData = chartData,
                verticalAxisValues = verticalAxisValues,
                verticalAxisLabelTransform = { formatMm(it) }
            )
        }
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

private const val MAX_HORIZONTAL_LINE = 5

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun <T> ChartAnimatedContent(targetState: T, content: @Composable (target: T) -> Unit) {
    AnimatedContent(
        targetState = targetState,
        transitionSpec = {
            expandHorizontally(
                animationSpec = tween(DURATION_DOUBLE_LONG, delayMillis = DURATION_SMALL),
                expandFrom = Alignment.Start
            ) with shrinkHorizontally(
                animationSpec = tween(DURATION_SMALL),
                shrinkTowards = Alignment.Start
            )
        }
    ) { target ->
        content.invoke(target)
    }
}