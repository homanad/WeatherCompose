package com.homalab.android.compose.weather.presentation.ui.search

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.homalab.android.compose.weather.data.util.NetworkChecker
import com.homalab.android.compose.weather.domain.entity.City
import com.homalab.android.compose.weather.presentation.components.SearchBar
import com.homalab.android.compose.weather.presentation.ui.MainState
import com.homalab.android.compose.weather.presentation.ui.vm.MainViewModel
import com.homalab.android.compose.weather.util.IconPadding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TopBar(
    context: Context,
    mainState: MainState,
    searchState: SearchState<City>,
    viewModel: MainViewModel = hiltViewModel(),
    networkChecker: NetworkChecker
) {
    LaunchedEffect(
        key1 = mainState.permissionState.allPermissionsGranted,
        key2 = mainState.requestLocation
    ) {
        if (mainState.permissionState.allPermissionsGranted) {
            if (mainState.requestLocation) {
                mainState.requestLocation = false
                getCurrentLocation(context) {
                    CoroutineScope(Dispatchers.IO).launch {
                        mainState.weatherData =
                            viewModel.getCurrentWeather(-1, it.latitude, it.longitude)
                        println("-------test: ${viewModel.getForecastData(it.latitude, it.longitude)}")
                    }
                }
            }
        } else mainState.permissionState.launchMultiplePermissionRequest()
    }

    LaunchedEffect(searchState.query.text) {
        if (searchState.query.text.isEmpty()) return@LaunchedEffect

        searchState.searching = true
        if (searchState.searching) {
            delay(500)
        }
        searchState.searchResults =
            if (networkChecker.getConnectionType() != NetworkChecker.NONE)
                viewModel.search(searchState.query.text)
            else null
        searchState.searching = false
    }

    BackHandler {
        if (searchState.focused) searchState.focused = false
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchBar(
            query = searchState.query,
            onQueryChange = { searchState.query = it },
            onSearchFocusChange = {
                if (!searchState.focused) searchState.query = TextFieldValue("")
                searchState.focused = it
            },
            onClearQuery = { searchState.query = TextFieldValue("") },
            onBack = { searchState.focused = false },
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
}

private fun getCurrentLocation(context: Context, onComplete: (LatLng) -> Unit) {
    val locationService = LocationServices.getFusedLocationProviderClient(context)
    locationService.lastLocation.addOnCompleteListener {
        onComplete.invoke(LatLng(it.result.latitude, it.result.longitude))
    }
}