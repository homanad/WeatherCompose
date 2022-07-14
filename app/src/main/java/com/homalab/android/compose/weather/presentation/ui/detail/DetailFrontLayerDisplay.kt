package com.homalab.android.compose.weather.presentation.ui.detail

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.homalab.android.compose.weather.R
import com.homalab.android.compose.weather.domain.entity.subEntity.ForecastItem
import com.homalab.android.compose.weather.presentation.components.*
import com.homalab.android.compose.weather.presentation.mapper.ForecastDayItem
import com.homalab.android.compose.weather.presentation.theme.CloudsColor
import com.homalab.android.compose.weather.presentation.theme.HumidityColor
import com.homalab.android.compose.weather.presentation.theme.PressureColor
import com.homalab.android.compose.weather.presentation.theme.SeaLevelColor
import com.homalab.android.compose.weather.util.*

@Composable
fun DetailFrontLayerDisplay(
    forecastDayItem: ForecastDayItem?,
    modifier: Modifier = Modifier
) {
    if (forecastDayItem != null) {
        DetailFrontLayerInfo(forecastDayItem = forecastDayItem, modifier)
    } else {
        MessageText(
            text = stringResource(id = R.string.network_unavailable)
        )
    }
}

@Composable
fun DetailFrontLayerInfo(forecastDayItem: ForecastDayItem, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {

        RoundedLine(modifier = Modifier.align(Alignment.CenterHorizontally))

        DefaultVerticalSpacer()

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = TimeFormatter.formatDetailDay(forecastDayItem.dt, forecastDayItem.timeZone),
            textAlign = TextAlign.Center
        )

        SmallVerticalSpacer()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimension2)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.Center
        ) {
            forecastDayItem.list.forEach {
                DetailConditionCard(
                    modifier = Modifier
                        .padding(start = Dimension1, end = Dimension1)
                        .width(DetailCardWidth),
                    forecastItem = it,
                    timeZone = forecastDayItem.timeZone
                )
            }
        }

        HumidityChart(forecastItems = forecastDayItem.list, timeZone = forecastDayItem.timeZone)

        SeaChart(forecastItems = forecastDayItem.list, timeZone = forecastDayItem.timeZone)
    }
}

@Composable
fun HumidityChart(forecastItems: List<ForecastItem>, timeZone: Int) {
    TitledChart(
        title = stringResource(id = R.string.clouds) + " & " + stringResource(id = R.string.humidity),
        modifier = titledChartModifier
    ) {
        val cloudsValues = mutableListOf<MultipleChartValue>()
        val humidityValues = mutableListOf<MultipleChartValue>()

        forecastItems.forEach { forecastItem ->
            cloudsValues.add(
                MultipleChartValue(
                    value = forecastItem.clouds.all.toFloat(),
                    label = TimeFormatter.formatChartTime(
                        forecastItem.dt,
                        timeZone
                    )
                )
            )
            humidityValues.add(
                MultipleChartValue(
                    value = forecastItem.main.humidity.toFloat(),
                    label = TimeFormatter.formatChartTime(
                        forecastItem.dt,
                        timeZone
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
            horizontalLineStyle = HorizontalLineStyle.STROKE
        )
    }
}

@Composable
fun SeaChart(forecastItems: List<ForecastItem>, timeZone: Int) {
    TitledChart(
        title = stringResource(id = R.string.sea_level) + " & " + stringResource(id = R.string.pressure),
        modifier = titledChartModifier
    ) {
        val seaLevelValues = mutableListOf<MultipleChartValue>()
        val pressureValues = mutableListOf<MultipleChartValue>()

        forecastItems.forEach { forecastItem ->
            seaLevelValues.add(
                MultipleChartValue(
                    value = forecastItem.main.sea_level.toFloat(),
                    label = TimeFormatter.formatChartTime(
                        forecastItem.dt,
                        timeZone
                    )
                )
            )
            pressureValues.add(
                MultipleChartValue(
                    value = forecastItem.main.sea_level.toFloat(),
                    label = TimeFormatter.formatChartTime(
                        forecastItem.dt,
                        timeZone
                    )
                )
            )
        }

        val seaLevelData = MultipleChartData(
            dotColor = SeaLevelColor,
            lineColor = SeaLevelColor,
            values = seaLevelValues,
            label = stringResource(id = R.string.sea_level),
            dotRatio = 2f
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
            showHorizontalLines = false,
            horizontalLineStyle = HorizontalLineStyle.STROKE
        )
    }
}