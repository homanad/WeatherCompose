package com.homalab.android.compose.weather.presentation.ui.search

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.homalab.android.compose.weather.domain.entity.City
import com.homalab.android.compose.weather.presentation.ui.MainState
import com.homalab.android.compose.weather.presentation.components.SearchBar
import com.homalab.android.compose.weather.presentation.components.SearchState
import com.homalab.android.compose.weather.util.IconPadding

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TopBar(mainState: MainState, searchState: SearchState<City>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
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
}