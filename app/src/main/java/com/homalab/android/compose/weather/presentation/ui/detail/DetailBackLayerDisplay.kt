package com.homalab.android.compose.weather.presentation.ui.detail

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.homalab.android.compose.weather.R
import com.homalab.android.compose.weather.presentation.components.*
import com.homalab.android.compose.weather.presentation.components.charts.*
import com.homalab.android.compose.weather.presentation.components.charts.components.generateVerticalValues
import com.homalab.android.compose.weather.presentation.mapper.ForecastDayItem
import com.homalab.android.compose.weather.presentation.theme.RainColor
import com.homalab.android.compose.weather.presentation.theme.TemperatureMaxColor
import com.homalab.android.compose.weather.presentation.theme.TemperatureMinColor
import com.homalab.android.compose.weather.presentation.theme.TemperatureNormalColor
import com.homalab.android.compose.weather.util.*

@Composable
fun DetailBackLayerDisplay(
    forecastDayItem: ForecastDayItem?,
    modifier: Modifier = Modifier,
    detailState: DetailState
) {
    DefaultVerticalSpacer()
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = detailState.isRefreshing),
        onRefresh = { detailState.isRefreshing = true }) {
        if (forecastDayItem != null) {
            DetailBackLayerInfo(forecastDayItem = forecastDayItem, modifier)
        } else {
            Column(modifier = Modifier.clickable { detailState.isRefreshing = true }) {
                MessageText(text = stringResource(id = R.string.network_unavailable))
                MessageText(text = stringResource(id = R.string.tap_to_reload))
            }
        }
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

@OptIn(ExperimentalAnimationApi::class)
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

    TitledChart(
        title = stringResource(id = R.string.temperature),
        modifier = titledChartModifier,
        lineColors = LineColors(TemperatureMinColor, TemperatureMaxColor)
    ) {
        ChartAnimatedContent(targetState = multipleChartData) { chartData ->
            MultipleLinesChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimension4),
                chartData = chartData,
                verticalAxisLabelTransform = {
                    formatCDegree(it)
                },
                animationOptions = ChartDefaults.defaultAnimationOptions().copy(isEnabled = true)
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
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

    var verticalAxisValues = generateVerticalValues(
        barChartData.minOf { it.barValue },
        barChartData.maxOf { it.barValue })

    if (verticalAxisValues.isEmpty()) verticalAxisValues = mutableListOf(0f, 1f)

    TitledChart(
        title = stringResource(id = R.string.rain),
        modifier = titledChartModifier,
        lineColors = LineColors(RainColor, RainColor)
    ) {
        ChartAnimatedContent(targetState = barChartData) { chartData ->
            BarChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimension4),
                chartData = chartData,
                verticalAxisValues = verticalAxisValues.toMutableList(),
                verticalAxisLabelTransform = { formatMm(it) },
                animationOptions = ChartDefaults.AnimationOptions(true, durationMillis = 300)
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun <T> ChartAnimatedContent(
    targetState: T,
    animationSpec: ContentTransform = getFadeInOutContentTransform(),
    content: @Composable (target: T) -> Unit
) {
    AnimatedContent(
        targetState = targetState,
        transitionSpec = { animationSpec }
    ) { target ->
        content.invoke(target)
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun getHorizontalExpandContentTransform() = expandHorizontally(
    animationSpec = tween(DURATION_DOUBLE_LONG, delayMillis = DURATION_SMALL),
    expandFrom = Alignment.Start
) with shrinkHorizontally(
    animationSpec = tween(DURATION_SMALL),
    shrinkTowards = Alignment.Start
)

@OptIn(ExperimentalAnimationApi::class)
fun getFadeInOutContentTransform() = fadeIn(
    animationSpec = tween(DURATION_SMALL)
) with fadeOut(
    animationSpec = tween(DURATION_SMALL)
)

fun testMultipleBars(): List<MultipleBarsChartData> {
    return listOf(
        MultipleBarsChartData(
            values = listOf(
                MultipleBarsChartValue(
                    barColor = Color.Blue,
                    value = 10f
                ),
                MultipleBarsChartValue(
                    barColor = Color.Gray,
                    value = 11f
                ),
                MultipleBarsChartValue(
                    barColor = Color.Green,
                    value = 14f
                )
            ),
            label = "label1"
        ),
        MultipleBarsChartData(
            values = listOf(
                MultipleBarsChartValue(
                    barColor = Color.Blue,
                    value = 20f
                ),
                MultipleBarsChartValue(
                    barColor = Color.Gray,
                    value = 15f
                ),
                MultipleBarsChartValue(
                    barColor = Color.Green,
                    value = 16f
                )
            ),
            label = "label2"
        ),
        MultipleBarsChartData(
            values = listOf(
                MultipleBarsChartValue(
                    barColor = Color.Blue,
                    value = 10f
                ),
                MultipleBarsChartValue(
                    barColor = Color.Gray,
                    value = 11f
                ),
                MultipleBarsChartValue(
                    barColor = Color.Green,
                    value = 14f
                )
            ),
            label = "label3"
        )
    )
}