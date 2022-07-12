package com.homalab.android.compose.weather.presentation.ui.home

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.homalab.android.compose.weather.data.util.NetworkChecker
import com.homalab.android.compose.weather.domain.entity.City
import com.homalab.android.compose.weather.presentation.components.AppBottomSheetScaffold
import com.homalab.android.compose.weather.presentation.ui.MainState
import com.homalab.android.compose.weather.presentation.ui.home.search.SearchDisplay
import com.homalab.android.compose.weather.presentation.ui.home.search.SearchState
import com.homalab.android.compose.weather.presentation.ui.home.search.TopBar
import com.homalab.android.compose.weather.presentation.ui.home.search.rememberSearchState
import com.homalab.android.compose.weather.presentation.ui.home.weather.WeatherDisplay
import com.homalab.android.compose.weather.presentation.ui.vm.MainViewModel
import com.homalab.android.compose.weather.util.Dimension2
import com.homalab.android.compose.weather.util.RecentlyBottomSheetPeekHeight
import com.homalab.android.compose.weather.util.getDisplayEnterTransition
import com.homalab.android.compose.weather.util.getDisplayExitTransition

@OptIn(
    ExperimentalAnimationApi::class, ExperimentalPermissionsApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun HomeScreen(
    context: Context,
    networkChecker: NetworkChecker,
    mainState: MainState,
    viewModel: MainViewModel = hiltViewModel(),
    searchState: SearchState<City> = rememberSearchState(),
    onDetailClick: (lat: Double, lon: Double) -> Unit
) {
    LaunchedEffect(searchState.selectedItem) {
        if (searchState.selectedItem != null) {
            val item = searchState.selectedItem!!
            mainState.weatherData =
                viewModel.getCurrentWeather(item.id, item.coord.lat, item.coord.lon)
            mainState.forecastData = viewModel.getForecastData(item.coord.lat, item.coord.lon)
        } else {
            mainState.weatherData = viewModel.getLastWeather()
            mainState.weatherData?.let {
                mainState.forecastData = viewModel.getForecastData(it.coord.lat, it.coord.lon)
            }
        }
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
        },
        backgroundColor = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = RecentlyBottomSheetPeekHeight + Dimension2)
        ) {
            TopBar(
                mainState = mainState,
                searchState = searchState,
                context = context,
                networkChecker = networkChecker
            )

            AnimatedContent(
                targetState = searchState.focused,
                transitionSpec = {
                    getDisplayEnterTransition() with getDisplayExitTransition()
                }
            ) { isFocused ->
                if (isFocused) {
                    SearchDisplay(searchState = searchState)
                } else {
                    WeatherDisplay(mainState = mainState) {
                        onDetailClick(
                            mainState.weatherData!!.coord.lat,
                            mainState.weatherData!!.coord.lon
                        )
                    }
                }
            }
        }
    }
}