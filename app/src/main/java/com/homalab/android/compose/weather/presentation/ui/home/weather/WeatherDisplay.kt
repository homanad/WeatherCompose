package com.homalab.android.compose.weather.presentation.ui.home.weather

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.homalab.android.compose.constants.GlobalConstants
import com.homalab.android.compose.weather.R
import com.homalab.android.compose.weather.domain.entity.WeatherData
import com.homalab.android.compose.weather.presentation.components.*
import com.homalab.android.compose.weather.presentation.ui.MainState
import com.homalab.android.compose.weather.presentation.ui.vm.MainViewModel
import com.homalab.android.compose.weather.util.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherDisplay(mainState: MainState, onDetailClick: () -> Unit) {
    if (mainState.weatherData != null) {
        WeatherInfo(mainState.weatherData!!, mainState, onDetailClick)
    } else {
        MessageText(
            text = stringResource(id = R.string.empty_weather_holder),
            modifier = Modifier.clickable {
                mainState.requestLocation = true
                if (!mainState.permissionState.allPermissionsGranted) mainState.permissionState.launchMultiplePermissionRequest()

            }
        )
    }
}

@Composable
private fun WeatherInfo(
    weatherData: WeatherData,
    mainState: MainState,
    onDetailClick: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    LaunchedEffect(mainState.isRefreshing) {
        if (mainState.isRefreshing) {
            mainState.weatherData?.let {
                mainState.weatherData =
                    viewModel.getCurrentWeather(it.id, it.coord.lat, it.coord.lon)
                mainState.forecastData = viewModel.getForecastData(it.coord.lat, it.coord.lon)
            }
            mainState.isRefreshing = false
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(mainState.isRefreshing),
        onRefresh = { mainState.isRefreshing = true }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            val weather = weatherData.weather[0]

            LargeVerticalSpacer()

            Text(text = weatherData.name, style = MaterialTheme.typography.headlineLarge)

            DefaultVerticalSpacer()

            Text(text = TimeFormatter.formatFullTime(weatherData.dt, weatherData.timeZone))

            LargeVerticalSpacer()

            Text(
                text = formatCDegree(weatherData.main.temp),
                style = MaterialTheme.typography.displayLarge
            )

            LargeVerticalSpacer()

//            Image(
//                painter = rememberAsyncImagePainter(
//                    Constants.OPEN_WEATHER_ICON_URL_PATTERN.format(
//                        weather.icon
//                    )
//                ),
//                contentDescription = null,
//                modifier = Modifier.size(WeatherConditionImageSize)
//            )
            WeatherConditionLoader(
                conditionId = weather.id,
                modifier = Modifier.size(WeatherConditionImageSize)
            )

            LargeVerticalSpacer()

            Text(
                text = formatCondition(weather.main, weather.description),
                style = MaterialTheme.typography.titleLarge
            )

            DefaultVerticalSpacer()

            Text(
                text = formatMinMaxDegree(weatherData.main.temp_min, weatherData.main.temp_max),
                style = MaterialTheme.typography.titleMedium
            )

            DefaultVerticalSpacer()

            Row {
                weatherData.rain?.let {
                    RainOrSnowCard(
                        title = stringResource(id = R.string.rain),
                        rainOrSnow = weatherData.rain,
                        modifier = Modifier.padding(Dimension2)
                    )
                }
                weatherData.snow?.let {
                    RainOrSnowCard(
                        title = stringResource(id = R.string.snow),
                        rainOrSnow = weatherData.snow,
                        modifier = Modifier.padding(Dimension2)
                    )
                }
            }

            val cardModifier = Modifier.padding(Dimension2)
            Row {
                HomeConditionCard(
                    cardModifier,
                    title = stringResource(id = R.string.feels_like),
                    description = formatCDegree(weatherData.main.feels_like)
                )

                HomeConditionCard(
                    cardModifier,
                    title = stringResource(id = R.string.pressure),
                    description = formatHPa(weatherData.main.pressure)
                )

                HomeConditionCard(
                    cardModifier,
                    title = stringResource(id = R.string.humidity),
                    description = formatPercent(weatherData.main.humidity)
                )
            }

            Row {
                HomeConditionCard(
                    cardModifier,
                    title = stringResource(id = R.string.sunrise),
                    description = TimeFormatter.formatSunEventTime(
                        weatherData.sys.sunrise,
                        weatherData.timeZone
                    )
                )

                HomeConditionCard(
                    cardModifier,
                    title = stringResource(id = R.string.wind),
                    description = formatWind(weatherData.wind.speed, weatherData.wind.deg)
                )

                HomeConditionCard(
                    cardModifier,
                    title = stringResource(id = R.string.sunset),
                    description = TimeFormatter.formatSunEventTime(
                        weatherData.sys.sunset,
                        weatherData.timeZone
                    )
                )
            }

            Column(
                modifier = Modifier.clickable { onDetailClick.invoke() }.semantics { contentDescription = GlobalConstants.DetailButtonDescription },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.click_details),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimension1),
                    textAlign = TextAlign.Center
                )
                Icon(imageVector = Icons.Filled.KeyboardArrowDown, contentDescription = null)
            }
        }
    }
}