package com.homalab.android.compose.weather.presentation.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.homalab.android.compose.weather.domain.entity.subEntity.ForecastItem
import com.homalab.android.compose.weather.presentation.components.DefaultSpacer
import com.homalab.android.compose.weather.presentation.components.WeatherConditionLoader
import com.homalab.android.compose.weather.util.Constants
import com.homalab.android.compose.weather.util.Dimension2
import com.homalab.android.compose.weather.util.TimeFormatter
import com.homalab.android.compose.weather.util.WeatherDayConditionImageSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailConditionCard(modifier: Modifier = Modifier, forecastItem: ForecastItem, timeZone: Int) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(Dimension2),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = forecastItem.weather[0].main,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = forecastItem.weather[0].description,
                style = MaterialTheme.typography.titleSmall
            )
//            Image(
//                painter = rememberAsyncImagePainter(
//                    Constants.OPEN_WEATHER_ICON_URL_PATTERN.format(
//                        forecastItem.weather[0].icon
//                    )
//                ),
//                contentDescription = null,
//                modifier = Modifier.size(WeatherDayConditionImageSize)
//            )
            WeatherConditionLoader(
                conditionId = forecastItem.weather[0].id,
                modifier = Modifier.size(WeatherDayConditionImageSize)
            )

            Text(
                text = Constants.C_DEGREE_PATTERN.format(forecastItem.main.feels_like),
                style = MaterialTheme.typography.titleLarge
            )

            DefaultSpacer()

            Text(text = TimeFormatter.formatChartTime(forecastItem.dt, timeZone))

//            DefaultSpacer()
//
//            Text(text = "Clouds: ${forecastItem.clouds.all}%")

        }
    }
}