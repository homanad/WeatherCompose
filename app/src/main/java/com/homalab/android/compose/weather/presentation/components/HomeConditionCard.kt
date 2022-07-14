package com.homalab.android.compose.weather.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.homalab.android.compose.weather.R
import com.homalab.android.compose.weather.domain.entity.subEntity.ForecastItem
import com.homalab.android.compose.weather.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeConditionCard(modifier: Modifier = Modifier, title: String, description: String) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(Dimension4),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            DefaultVerticalSpacer()
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}


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
                text = formatCDegree(forecastItem.main.feels_like),
                style = MaterialTheme.typography.titleLarge
            )

            DefaultVerticalSpacer()

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_visibility),
                    contentDescription = null,
                    modifier = Modifier.size(VisibilityImageSize)
                )

                SmallHorizontalSpacer()

                Text(text = transformVisibility(forecastItem.visibility))
            }

            DefaultVerticalSpacer()

            Text(text = TimeFormatter.formatChartTime(forecastItem.dt, timeZone))
        }
    }
}