package com.homalab.android.compose.weather.presentation.ui.detail

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.homalab.android.compose.weather.R
import com.homalab.android.compose.weather.presentation.components.*
import com.homalab.android.compose.weather.presentation.mapper.ForecastDayItem
import com.homalab.android.compose.weather.util.Dimension1
import com.homalab.android.compose.weather.util.Dimension2
import com.homalab.android.compose.weather.util.Dimension4
import com.homalab.android.compose.weather.util.TimeFormatter

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
                modifier = titledChardModifier
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
                            value = forecastItem.main.humidity,
                            label = TimeFormatter.formatChartTime(
                                forecastItem.dt,
                                forecastDayItem.timeZone
                            )
                        )
                    )
                }

                val cloudsData = MultipleChartData(
                    dotColor = Color.Black,
                    lineColor = Color.Black,
                    values = cloudsValues
                )

                val humidityData = MultipleChartData(
                    dotColor = Color.Green,
                    lineColor = Color.Green,
                    values = humidityValues
                )

                MultipleLinesChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimension4),
                    chartData = listOf(cloudsData, humidityData),
                    verticalAxisValues = listOf(0f, 20f, 40f, 60f, 80f, 100f),
                    verticalAxisLabelTransform = { it.toInt().toString() + " %" },
                    showHorizontalLines = false
                )
            }
        }
    }
}