package com.homalab.android.compose.weather.presentation.ui.home.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.homalab.android.compose.weather.R
import com.homalab.android.compose.weather.domain.entity.WeatherData
import com.homalab.android.compose.weather.presentation.components.ConditionCard
import com.homalab.android.compose.weather.presentation.components.DefaultSpacer
import com.homalab.android.compose.weather.presentation.components.LargeSpacer
import com.homalab.android.compose.weather.presentation.components.MessageText
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

            LargeSpacer()

            Text(text = weatherData.name, style = MaterialTheme.typography.headlineLarge)

            DefaultSpacer()

            Text(text = TimeFormatter.formatFullTime(weatherData.dt, weatherData.timeZone))

            LargeSpacer()

            Text(
                text = Constants.C_DEGREE_PATTERN.format(weatherData.main.temp),
                style = MaterialTheme.typography.displayLarge
            )

            LargeSpacer()

            Image(
                painter = rememberAsyncImagePainter(
                    Constants.OPEN_WEATHER_ICON_URL_PATTERN.format(
                        weather.icon
                    )
                ),
                contentDescription = null,
                modifier = Modifier.size(WeatherConditionImageSize)
            )

            LargeSpacer()

            Text(
                text = Constants.CONDITION_PATTERN.format(weather.main, weather.description),
                style = MaterialTheme.typography.titleLarge
            )

            DefaultSpacer()

            Text(
                text = Constants.C_DEGREE_MIN_MAX_PATTERN.format(
                    weatherData.main.temp_min,
                    weatherData.main.temp_max
                ), style = MaterialTheme.typography.titleMedium
            )

            DefaultSpacer()

            val cardModifier = Modifier.padding(Dimension2)

            Row {
                ConditionCard(
                    cardModifier,
                    title = stringResource(id = R.string.feels_like),
                    description = weatherData.main.feels_like.toString()
                )

                ConditionCard(
                    cardModifier,
                    title = stringResource(id = R.string.pressure),
                    description = weatherData.main.pressure.toString()
                )

                ConditionCard(
                    cardModifier,
                    title = stringResource(id = R.string.humidity),
                    description = weatherData.main.humidity.toString()
                )
            }

            Row {
                ConditionCard(
                    cardModifier,
                    title = stringResource(id = R.string.sunrise),
                    description = TimeFormatter.formatSunEventTime(
                        weatherData.sys.sunrise,
                        weatherData.timeZone
                    )
                )

                ConditionCard(
                    cardModifier,
                    title = stringResource(id = R.string.wind),
                    description = Constants.WIND_PATTERN.format(
                        weatherData.wind.speed,
                        weatherData.wind.deg
                    )
                )

                ConditionCard(
                    cardModifier,
                    title = stringResource(id = R.string.sunset),
                    description = TimeFormatter.formatSunEventTime(
                        weatherData.sys.sunset,
                        weatherData.timeZone
                    )
                )
            }

            Column(
                modifier = Modifier.clickable { onDetailClick.invoke() },
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