package com.homalab.android.compose.weather.presentation.ui.detail

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.homalab.android.compose.weather.R
import com.homalab.android.compose.weather.presentation.components.*
import com.homalab.android.compose.weather.presentation.mapper.ForecastDayItem
import com.homalab.android.compose.weather.presentation.theme.CloudsColor
import com.homalab.android.compose.weather.presentation.theme.HumidityColor
import com.homalab.android.compose.weather.presentation.theme.PressureColor
import com.homalab.android.compose.weather.presentation.theme.SeaLevelColor
import com.homalab.android.compose.weather.util.*

@Composable
fun DetailFrontLayerDisplay(
    forecastDayItem: ForecastDayItem?
) {
    Column {
//        Text(
//            modifier = Modifier.fillMaxWidth(),
//            text = "This day",
//            textAlign = TextAlign.Center
//        )

//        SmallSpacer()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimension2)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.Center
        ) {
            forecastDayItem?.list?.forEach {
                DetailConditionCard(
                    modifier = Modifier.padding(start = Dimension1, end = Dimension1),
                    forecastItem = it,
                    timeZone = forecastDayItem.timeZone
                )
            }
        }

        forecastDayItem?.list?.let {
            TitledChart(
                title = stringResource(id = R.string.clouds) + " & " + stringResource(id = R.string.humidity),
                modifier = titledChartModifier
            ) {
                val cloudsValues = mutableListOf<MultipleChartValue>()
                val humidityValues = mutableListOf<MultipleChartValue>()

                it.forEach { forecastItem ->
                    cloudsValues.add(
                        MultipleChartValue(
                            value = forecastItem.clouds.all.toFloat(),
                            label = TimeFormatter.formatChartTime(
                                forecastItem.dt,
                                forecastDayItem.timeZone
                            )
                        )
                    )
                    humidityValues.add(
                        MultipleChartValue(
                            value = forecastItem.main.humidity.toFloat(),
                            label = TimeFormatter.formatChartTime(
                                forecastItem.dt,
                                forecastDayItem.timeZone
                            )
                        )
                    )
                }

                val cloudsData = MultipleChartData(
                    dotColor = CloudsColor,
                    lineColor = CloudsColor,
                    values = cloudsValues,
                    label = stringResource(id = R.string.clouds)
                )

                val humidityData = MultipleChartData(
                    dotColor = HumidityColor,
                    lineColor = HumidityColor,
                    values = humidityValues,
                    label = stringResource(id = R.string.humidity)
                )

                MultipleLinesChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimension4),
                    chartData = listOf(cloudsData, humidityData),
                    verticalAxisValues = listOf(0f, 20f, 40f, 60f, 80f, 100f),
                    verticalAxisLabelTransform = { formatPercent(it.toInt()) },
                    showHorizontalLines = true,
                    horizontalLineStyle = HorizontalLineStyle.STROKE,
                    drawCirclePoint = false
                )
            }

            TitledChart(title = stringResource(id = R.string.sea_level) + " & " + stringResource(id = R.string.pressure)) {
                val seaLevelValues = mutableListOf<MultipleChartValue>()
                val pressureValues = mutableListOf<MultipleChartValue>()

                it.forEach { forecastItem ->
                    seaLevelValues.add(
                        MultipleChartValue(
                            value = forecastItem.main.sea_level.toFloat(),
                            label = TimeFormatter.formatChartTime(
                                forecastItem.dt,
                                forecastDayItem.timeZone
                            )
                        )
                    )
                    pressureValues.add(
                        MultipleChartValue(
                            value = forecastItem.main.sea_level.toFloat(),
                            label = TimeFormatter.formatChartTime(
                                forecastItem.dt,
                                forecastDayItem.timeZone
                            )
                        )
                    )
                }

                val seaLevelData = MultipleChartData(
                    dotColor = SeaLevelColor,
                    lineColor = SeaLevelColor,
                    values = seaLevelValues,
                    label = stringResource(id = R.string.sea_level)
                )

                val pressureData = MultipleChartData(
                    dotColor = PressureColor,
                    lineColor = PressureColor,
                    values = pressureValues,
                    label = stringResource(id = R.string.pressure)
                )

                MultipleLinesChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimension4),
                    chartData = listOf(seaLevelData, pressureData),
                    verticalAxisValues = generateMinMaxRange(seaLevelValues.map { it.value }
                        .minOf { it }, seaLevelValues.map { it.value }.maxOf { it }),
                    verticalAxisLabelTransform = { formatHPa(it.toInt()) },
                    showHorizontalLines = true,
                    horizontalLineStyle = HorizontalLineStyle.STROKE,
                    drawCirclePoint = false
                )
            }
        }
    }
}