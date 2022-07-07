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
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
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
import com.homalab.android.compose.weather.R
import com.homalab.android.compose.weather.data.util.NetworkChecker
import com.homalab.android.compose.weather.domain.entity.City
import com.homalab.android.compose.weather.domain.entity.WeatherData
import com.homalab.android.compose.weather.presentation.components.AppBottomSheetScaffold
import com.homalab.android.compose.weather.presentation.theme.WeatherComposeTheme
import com.homalab.android.compose.weather.presentation.ui.home.RecentlyViewedBottomSheetContent
import com.homalab.android.compose.weather.presentation.ui.home.RecentlyViewedBottomSheetShape
import com.homalab.android.compose.weather.presentation.ui.search.SearchDisplay
import com.homalab.android.compose.weather.presentation.ui.search.SearchState
import com.homalab.android.compose.weather.presentation.ui.search.TopBar
import com.homalab.android.compose.weather.presentation.ui.search.rememberSearchState
import com.homalab.android.compose.weather.presentation.ui.vm.MainViewModel
import com.homalab.android.compose.weather.presentation.ui.weather.WeatherDisplay
import com.homalab.android.compose.weather.util.BackgroundImageAlpha
import com.homalab.android.compose.weather.util.RecentlyBottomSheetPeekHeight
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
                    WeatherApp(this, networkChecker)
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalPermissionsApi::class,
    ExperimentalMaterialApi::class
)
@Composable
private fun WeatherApp(
    context: Context,
    networkChecker: NetworkChecker,
    viewModel: MainViewModel = hiltViewModel(),
    mainState: MainState = rememberMainState(),
    searchState: SearchState<City> = rememberSearchState()
) {
    LaunchedEffect(searchState.selectedItem) {
        if (searchState.selectedItem != null) {
            val item = searchState.selectedItem!!
            mainState.weatherData =
                viewModel.getCurrentWeather(item.id, item.coord.lat, item.coord.lon)
        } else mainState.weatherData = viewModel.getLastWeather()
    }

    LaunchedEffect(mainState.weatherData) {
        mainState.savedCities = viewModel.getSavedWeathers()
        searchState.suggestions = mainState.savedCities ?: listOf()
    }

    val state =
        rememberBottomSheetScaffoldState(bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed))
    AppBottomSheetScaffold(
        scaffoldState = state,
        sheetShape = RecentlyViewedBottomSheetShape,
        sheetPeekHeight = RecentlyBottomSheetPeekHeight,
        sheetContent = {
            RecentlyViewedBottomSheetContent(
                itemList = mainState.savedCities ?: listOf(),
                onItemClick = { searchState.selectedItem = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(MaterialTheme.colorScheme.primaryContainer)
            )
        }
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
                TopBar(
                    mainState = mainState,
                    searchState = searchState,
                    context = context,
                    networkChecker = networkChecker
                )

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

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    WeatherComposeTheme {
//        WeatherApp()
//    }
//}