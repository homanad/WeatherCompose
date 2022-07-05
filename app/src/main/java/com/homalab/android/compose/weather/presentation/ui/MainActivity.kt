package com.homalab.android.compose.weather.presentation.ui

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.homalab.android.compose.weather.R
import com.homalab.android.compose.weather.data.mapper.toCity
import com.homalab.android.compose.weather.data.util.NetworkChecker
import com.homalab.android.compose.weather.domain.entity.City
import com.homalab.android.compose.weather.domain.entity.WeatherData
import com.homalab.android.compose.weather.presentation.components.RecentlyBottomSheetScaffold
import com.homalab.android.compose.weather.presentation.components.SearchState
import com.homalab.android.compose.weather.presentation.components.rememberSearchState
import com.homalab.android.compose.weather.presentation.theme.WeatherComposeTheme
import com.homalab.android.compose.weather.presentation.ui.search.SearchDisplay
import com.homalab.android.compose.weather.presentation.ui.search.TopBar
import com.homalab.android.compose.weather.presentation.ui.vm.MainViewModel
import com.homalab.android.compose.weather.presentation.ui.weather.WeatherDisplay
import com.homalab.android.compose.weather.util.BackgroundImageAlpha
import com.homalab.android.compose.weather.util.isInRange
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@ExperimentalAnimationApi
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

@ExperimentalAnimationApi
@ExperimentalPermissionsApi
@ExperimentalMaterial3Api
@Composable
private fun WeatherApp(
    context: Context,
    networkChecker: NetworkChecker,
    viewModel: MainViewModel = hiltViewModel(),
    mainState: MainState = rememberMainState(),
    searchState: SearchState<City> = rememberSearchState()
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

    LaunchedEffect(searchState.query.text) {
        searchState.searching = true
        delay(300)
        searchState.searchResults =
            if (networkChecker.getConnectionType() != NetworkChecker.NONE)
                viewModel.search(searchState.query.text)
            else null
        searchState.searching = false
    }

    LaunchedEffect(searchState.selectedItem) {
        searchState.selectedItem?.let {
            mainState.weatherData = viewModel.getCurrentWeather(it.id, it.coord.lat, it.coord.lon)
        }
    }

    LaunchedEffect(mainState.weatherData) {
        mainState.savedCity = viewModel.getSavedWeathers()?.map { it.toCity() }
    }

    LaunchedEffect(mainState.isRefreshing) {
        if (mainState.isRefreshing) {
            mainState.weatherData?.let {
                mainState.weatherData =
                    viewModel.getCurrentWeather(it.id, it.coord.lat, it.coord.lon)
            }
            mainState.isRefreshing = false
        }
    }

    LaunchedEffect(mainState.location) {
        mainState.location?.let {
            mainState.weatherData = viewModel.getCurrentWeather(-1, it.latitude, it.longitude)
        }
    }

    RecentlyBottomSheetScaffold(
        itemList = mainState.savedCity ?: listOf(),
        onItemClick = { searchState.selectedItem = it }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            mainState.backgroundResource?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    modifier = Modifier
                        .matchParentSize()
                        .alpha(BackgroundImageAlpha),
                    contentScale = ContentScale.FillBounds
                )
            }

            Column(modifier = Modifier.background(Color.Transparent)) {
                TopBar(mainState = mainState, searchState = searchState)

                AnimatedContent(targetState = searchState.focused) { isFocused ->
                    if (isFocused) {
                        SearchDisplay(searchState = searchState)
                    } else {
                        WeatherDisplay(mainState = mainState)
                    }
                }
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

@ExperimentalPermissionsApi
@Stable
class MainState(
    isRequestLocation: Boolean,
    location: LatLng?,
    weatherData: WeatherData?,
    savedCity: List<City>?,
    permissionState: MultiplePermissionsState,
    isRefreshing: Boolean
) {
    var requestLocation by mutableStateOf(isRequestLocation)
    var location by mutableStateOf(location)
    var weatherData by mutableStateOf(weatherData)
    var savedCity by mutableStateOf(savedCity)
    var permissionState by mutableStateOf(permissionState)
    var isRefreshing by mutableStateOf(isRefreshing)

    val backgroundResource: Int?
        get() {
            val id = weatherData?.weather?.get(0)?.id ?: 0
            return when {
                id.isInRange(200, 232) -> {
                    R.drawable.thunderstorm
                }
                id.isInRange(300, 321) -> {
                    R.drawable.dizzle
                }
                id.isInRange(500, 531) -> {
                    R.drawable.rain
                }
                id.isInRange(600, 622) -> {
                    R.drawable.snow
                }
                id.isInRange(701, 781) -> {
                    R.drawable.atmosphere
                }
                id == 800 -> {
                    R.drawable.clear
                }
                id.isInRange(801, 804) -> {
                    R.drawable.clouds
                }
                else -> null
            }
        }
}

@ExperimentalPermissionsApi
@Composable
fun rememberMainState(
    isRequestLocation: Boolean = false,
    location: LatLng? = null,
    weatherData: WeatherData? = null,
    savedCity: List<City> = listOf(),
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
            savedCity = savedCity,
            permissionState = permissionState,
            isRefreshing = false
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