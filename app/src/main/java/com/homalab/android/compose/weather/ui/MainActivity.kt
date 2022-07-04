package com.homalab.android.compose.weather.ui

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.homalab.android.compose.weather.data.util.NetworkChecker
import com.homalab.android.compose.weather.domain.entity.WeatherData
import com.homalab.android.compose.weather.ui.components.*
import com.homalab.android.compose.weather.ui.model.CityRecord
import com.homalab.android.compose.weather.ui.model.search
import com.homalab.android.compose.weather.ui.theme.WeatherComposeTheme
import com.homalab.android.compose.weather.ui.vm.MainViewModel
import com.homalab.android.compose.weather.util.Constants.CONDITION_PATTERN
import com.homalab.android.compose.weather.util.Constants.C_DEGREE_MIN_MAX_PATTERN
import com.homalab.android.compose.weather.util.Constants.C_DEGREE_PATTERN
import com.homalab.android.compose.weather.util.Constants.OPEN_WEATHER_ICON_URL_PATTERN
import com.homalab.android.compose.weather.util.Constants.WIND_PATTERN
import com.homalab.android.compose.weather.util.TimeFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@ExperimentalPermissionsApi
@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkChecker: NetworkChecker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherApp(this, networkChecker)
                }
            }
        }
    }
}

@ExperimentalPermissionsApi
@ExperimentalMaterial3Api
@Composable
private fun WeatherApp(
    context: Context,
    networkChecker: NetworkChecker,
    viewModel: MainViewModel = hiltViewModel(),
    mainState: MainState = rememberMainState(
        isRequestLocation = false,
        location = null,
        weatherData = null
    ),
    searchState: SearchState<CityRecord> = rememberSearchState()
) {

    if (mainState.requestLocation) {
        mainState.location = null
        mainState.requestLocation = false
        getCurrentLocation(context) {
            mainState.location = it
        }
    }

    LaunchedEffect(null) {
        mainState.weatherData = viewModel.getLastWeather()
    }

    LaunchedEffect(mainState.permissionState.allPermissionsGranted) {
        mainState.permissionState.launchMultiplePermissionRequest()
    }

    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            SearchBar(
                query = searchState.query,
                onQueryChange = { searchState.query = it },
                onSearchFocusChange = { searchState.focused = it },
                onClearQuery = { searchState.query = TextFieldValue("") },
                onBack = {
                    searchState.query = TextFieldValue("")
                    searchState.focused = false
                },
                searching = searchState.searching,
                focused = searchState.focused,
                modifier = Modifier.weight(1f),
            )

            IconButton(
                onClick = {
                    mainState.requestLocation = true
                    if (!mainState.permissionState.allPermissionsGranted) mainState.permissionState.launchMultiplePermissionRequest()
                },
                modifier = Modifier.padding(end = IconPadding)
            ) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)
            }
        }

        LaunchedEffect(searchState.query.text) {
            searchState.searching = true
            delay(100)
            searchState.searchResults =
                if (networkChecker.getConnectionType() != NetworkChecker.NONE) search(searchState.query.text) else null
            searchState.searching = false
        }

        LaunchedEffect(searchState.selectedItem) {
            searchState.selectedItem?.let {
                mainState.weatherData = viewModel.getCurrentWeather(
                    it.id,
                    it.coord.lat.toFloat(),
                    it.coord.lon.toFloat()
                )
            }
        }

        LaunchedEffect(mainState.location) {
            mainState.location?.let {
                mainState.weatherData = viewModel.getCurrentWeather(
                    -1,
                    it.latitude.toFloat(),
                    it.longitude.toFloat()
                )
            }
        }

        if (searchState.focused) {
            when (searchState.searchDisplay) {
                SearchDisplay.InitialResults -> {

                }
                SearchDisplay.NoResults -> {

                }
                SearchDisplay.NetworkUnavailable -> {
                    Text(
                        text = "Network unavailable",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(0.8f)
                            .padding(Dimension4),
                        textAlign = TextAlign.Center
                    )
                }
                SearchDisplay.Suggestions -> {

                }
                SearchDisplay.Results -> {
                    searchState.searchResults?.let {
                        SearchResultList(it) { city ->
                            searchState.selectedItem = city
                            searchState.query = TextFieldValue("")
                            searchState.focused = false
                        }
                    }
                }
            }
        } else {
            //display weather
            if (mainState.weatherData != null) {
                WeatherDisplay(mainState.weatherData!!)
            } else {
                Text(
                    text = "Start to search a city or click here to find your location",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(0.8f)
                        .padding(Dimension4)
                        .clickable {
                            mainState.requestLocation = true
                            if (!mainState.permissionState.allPermissionsGranted) mainState.permissionState.launchMultiplePermissionRequest()
                        },
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private fun getCurrentLocation(context: Context, onComplete: (LatLng) -> Unit) {
    val locationService = LocationServices.getFusedLocationProviderClient(context)
    locationService.lastLocation.addOnCompleteListener {
        onComplete.invoke(LatLng(it.result.latitude, it.result.longitude))
    }
}

@Composable
fun SearchResultList(itemList: List<CityRecord>, onItemClick: (CityRecord) -> Unit) {
    val scrollState = rememberLazyListState()

    LazyColumn(state = scrollState) {
        items(items = itemList, key = { it.id }) {
            CityRow(it, modifier = Modifier.clickable { onItemClick(it) })
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun WeatherDisplay(weatherData: WeatherData) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {

        LargeSpacer()

        Text(text = weatherData.name, style = MaterialTheme.typography.headlineLarge)

        DefaultSpacer()

        Text(text = TimeFormatter.formatFullTime(weatherData.dt, weatherData.timeZone))

        LargeSpacer()

        Text(
            text = C_DEGREE_PATTERN.format(weatherData.main.temp),
            style = MaterialTheme.typography.displayLarge
        )

        LargeSpacer()

        Image(
            painter = rememberAsyncImagePainter(OPEN_WEATHER_ICON_URL_PATTERN.format(weatherData.weather[0].icon)),
            contentDescription = null,
            modifier = Modifier.size(WeatherConditionImageSize)
        )

        LargeSpacer()

        Text(
            text = CONDITION_PATTERN.format(
                weatherData.weather[0].main,
                weatherData.weather[0].description
            ),
            style = MaterialTheme.typography.titleLarge
        )

        DefaultSpacer()

        Text(
            text = C_DEGREE_MIN_MAX_PATTERN.format(
                weatherData.main.temp_min,
                weatherData.main.temp_max
            ), style = MaterialTheme.typography.titleMedium
        )

        DefaultSpacer()

        val cardModifier = Modifier.padding(Dimension2)

        Row {
            ConditionCard(
                cardModifier,
                title = "Feels like",
                description = weatherData.main.feels_like.toString()
            )

            ConditionCard(
                cardModifier,
                title = "Pressure",
                description = weatherData.main.pressure.toString()
            )

            ConditionCard(
                cardModifier,
                title = "Humidity",
                description = weatherData.main.humidity.toString()
            )
        }

        Row {
            ConditionCard(
                cardModifier,
                title = "Sunrise",
                description = TimeFormatter.formatSunEventTime(
                    weatherData.sys.sunrise,
                    weatherData.timeZone
                )
            )

            ConditionCard(
                cardModifier,
                title = "Wind",
                description = WIND_PATTERN.format(weatherData.wind.speed, weatherData.wind.deg)
            )

            ConditionCard(
                cardModifier,
                title = "Sunset",
                description = TimeFormatter.formatSunEventTime(
                    weatherData.sys.sunset,
                    weatherData.timeZone
                )
            )
        }
    }
}

@ExperimentalPermissionsApi
@Stable
class MainState(
    isRequestLocation: Boolean,
    location: LatLng?,
    weatherData: WeatherData?,
    permissionState: MultiplePermissionsState
) {
    var requestLocation by mutableStateOf(isRequestLocation)
    var location by mutableStateOf(location)
    var weatherData by mutableStateOf(weatherData)
    var permissionState by mutableStateOf(permissionState)
}

@ExperimentalPermissionsApi
@Composable
fun rememberMainState(
    isRequestLocation: Boolean,
    location: LatLng?,
    weatherData: WeatherData?,
    permissionState: MultiplePermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
): MainState {
    return remember {
        MainState(
            isRequestLocation = isRequestLocation,
            location = location,
            weatherData = weatherData,
            permissionState = permissionState
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    WeatherComposeTheme {
//        WeatherApp()
//    }
//}