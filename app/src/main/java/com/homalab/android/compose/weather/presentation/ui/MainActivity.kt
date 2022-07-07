package com.homalab.android.compose.weather.presentation.ui

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.homalab.android.compose.weather.R
import com.homalab.android.compose.weather.data.util.NetworkChecker
import com.homalab.android.compose.weather.domain.entity.City
import com.homalab.android.compose.weather.domain.entity.WeatherData
import com.homalab.android.compose.weather.presentation.theme.WeatherComposeTheme
import com.homalab.android.compose.weather.presentation.ui.detail.DetailScreen
import com.homalab.android.compose.weather.presentation.ui.home.HomeScreen
import com.homalab.android.compose.weather.util.DoubleType
import com.homalab.android.compose.weather.util.isInRange
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
                    AppNavHost(this, networkChecker, rememberNavController())
                }
            }
        }
    }
}

@Composable
fun AppNavHost(
    context: Context,
    networkChecker: NetworkChecker,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavConstants.Screen.Home,
        modifier = modifier
    ) {
        composable(NavConstants.Screen.Home) {
            HomeScreen(
                context = context,
                networkChecker = networkChecker,
                onDetailClick = { lat, lon -> navigateToDetail(navController, lat, lon) })
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
            )
        ) { entry ->
            val lat = entry.arguments?.getDouble(NavConstants.Latitude) ?: 0.0
            val lon = entry.arguments?.getDouble(NavConstants.Longitude) ?: 0.0
            DetailScreen(lat, lon)
        }
    }
}

private fun navigateToDetail(navController: NavHostController, lat: Double, lon: Double) {
    val destination =  NavConstants.Screen.Detail
        .replace("{${NavConstants.Latitude}}", lat.toString())
        .replace("{${NavConstants.Longitude}}", lon.toString())
    navController.navigate(destination)
}

@OptIn(ExperimentalPermissionsApi::class)
@Stable
class MainState(
    isRequestLocation: Boolean,
    weatherData: WeatherData?,
    savedCity: List<City>?,
    permissionState: MultiplePermissionsState,
    isRefreshing: Boolean
) {
    var requestLocation by mutableStateOf(isRequestLocation)
    var weatherData by mutableStateOf(weatherData)
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

