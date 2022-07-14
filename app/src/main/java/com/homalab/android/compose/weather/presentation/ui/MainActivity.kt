package com.homalab.android.compose.weather.presentation.ui

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.homalab.android.compose.weather.R
import com.homalab.android.compose.weather.data.util.NetworkChecker
import com.homalab.android.compose.weather.domain.entity.City
import com.homalab.android.compose.weather.domain.entity.ForecastData
import com.homalab.android.compose.weather.domain.entity.WeatherData
import com.homalab.android.compose.weather.presentation.mapper.toForecastDayData
import com.homalab.android.compose.weather.presentation.theme.WeatherComposeTheme
import com.homalab.android.compose.weather.presentation.ui.detail.DetailScreen
import com.homalab.android.compose.weather.presentation.ui.detail.rememberDetailState
import com.homalab.android.compose.weather.presentation.ui.home.HomeScreen
import com.homalab.android.compose.weather.util.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@OptIn(ExperimentalAnimationApi::class)
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
                    AppNavHost(this, networkChecker, rememberAnimatedNavController())
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun AppNavHost(
    context: Context,
    networkChecker: NetworkChecker,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val mainState = rememberMainState()
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

        AnimatedNavHost(
            navController = navController,
            startDestination = NavConstants.Screen.Home,
            modifier = modifier
        ) {
            composable(
                route = NavConstants.Screen.Home,
//            enterTransition = { getSlideUpEnterTransition() },
                exitTransition = { getSlideUpExitTransition() },
                popEnterTransition = { getSlideDownEnterTransition() }
            ) {
                HomeScreen(
                    context = context,
                    networkChecker = networkChecker,
                    onDetailClick = { lat, lon -> navigateToDetail(navController, lat, lon) },
                    mainState = mainState
                )
            }
            composable(
                route = NavConstants.Screen.Detail,
                arguments = listOf(
                    navArgument(NavConstants.Latitude) {
                        type = DoubleType
                    },
                    navArgument(NavConstants.Longitude) {
                        type = DoubleType
                    }
                ),
                enterTransition = { getSlideUpEnterTransition() },
                popExitTransition = { getSlideDownExitTransition() }
            ) { entry ->
                val lat = entry.arguments?.getDouble(NavConstants.Latitude) ?: 0.0
                val lon = entry.arguments?.getDouble(NavConstants.Longitude) ?: 0.0
                DetailScreen(
                    lat,
                    lon,
                    rememberDetailState(mainState.forecastData?.toForecastDayData()),
                    onBackClick = { navController.navigateUp() }
                )
            }
        }
    }
}

private fun navigateToDetail(navController: NavHostController, lat: Double, lon: Double) {
    val destination = NavConstants.Screen.Detail
        .replace("{${NavConstants.Latitude}}", lat.toString())
        .replace("{${NavConstants.Longitude}}", lon.toString())
    navController.navigate(destination)
}

@OptIn(ExperimentalPermissionsApi::class)
@Stable
class MainState(
    isRequestLocation: Boolean,
    weatherData: WeatherData?,
    forecastData: ForecastData?,
    savedCity: List<City>?,
    permissionState: MultiplePermissionsState,
    isRefreshing: Boolean
) {
    var requestLocation by mutableStateOf(isRequestLocation)
    var weatherData by mutableStateOf(weatherData)
    var forecastData by mutableStateOf(forecastData)
    var savedCities by mutableStateOf(savedCity)
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberMainState(
    isRequestLocation: Boolean = false,
    weatherData: WeatherData? = null,
    forecastData: ForecastData? = null,
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
            weatherData = weatherData,
            forecastData = forecastData,
            savedCity = savedCity,
            permissionState = permissionState,
            isRefreshing = false
        )
    }
}

object NavConstants {
    const val Latitude = "lat"
    const val Longitude = "lon"

    object Screen {
        const val Home = "Home"
        const val Detail = "${Home}/{$Latitude}-{${Longitude}}"
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    WeatherComposeTheme {
//        WeatherApp()
//    }
//}

