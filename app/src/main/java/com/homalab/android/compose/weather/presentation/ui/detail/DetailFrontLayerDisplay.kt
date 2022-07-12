package com.homalab.android.compose.weather.presentation.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import coil.compose.rememberAsyncImagePainter
import com.homalab.android.compose.weather.presentation.components.DefaultSpacer
import com.homalab.android.compose.weather.presentation.mapper.ForecastDayItem
import com.homalab.android.compose.weather.util.Constants
import com.homalab.android.compose.weather.util.Dimension1
import com.homalab.android.compose.weather.util.TimeFormatter
import com.homalab.android.compose.weather.util.WeatherDayConditionImageSize

@Composable
fun DetailFrontLayerDisplay(
    forecastDayItem: ForecastDayItem?
) {
//    Surface(modifier = Modifier.fillMaxSize()) {
    Column {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "This day",
            textAlign = TextAlign.Center
        )

        DefaultSpacer()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .horizontalScroll(rememberScrollState())
        ) {
            forecastDayItem?.list?.forEach {
                Column(
                    modifier = Modifier.padding(Dimension1),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = it.weather[0].main)
                    Text(text = it.weather[0].description)
                    Image(
                        painter = rememberAsyncImagePainter(
                            Constants.OPEN_WEATHER_ICON_URL_PATTERN.format(
                                it.weather[0].icon
                            )
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(WeatherDayConditionImageSize)
                    )
                    Text(text = TimeFormatter.formatChartTime(it.dt, 0))

                }
            }
        }
//        }
    }
}