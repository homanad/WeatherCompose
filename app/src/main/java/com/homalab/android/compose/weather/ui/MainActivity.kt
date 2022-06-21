package com.homalab.android.compose.weather.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.homalab.android.compose.weather.domain.entity.WeatherData
import com.homalab.android.compose.weather.ui.components.*
import com.homalab.android.compose.weather.ui.model.CityRecord
import com.homalab.android.compose.weather.ui.model.search
import com.homalab.android.compose.weather.ui.theme.WeatherComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherApp()
                }
            }
        }
    }
}

@Composable
private fun WeatherApp(
    viewModel: MainViewModel = hiltViewModel(),
    state: SearchState<CityRecord> = rememberSearchState()
) {
    var weatherData: WeatherData? by remember { mutableStateOf(null) }

    Column {
        SearchBar(
            query = state.query,
            onQueryChange = { state.query = it },
            onSearchFocusChange = { state.focused = it },
            onClearQuery = { state.query = TextFieldValue("") },
            onBack = {
                state.query = TextFieldValue("")
                state.focused = false
            },
            searching = state.searching,
            focused = state.focused,
            modifier = Modifier,
        )

        LaunchedEffect(state.query.text) {
            state.searching = true
            delay(100)
            state.searchResults = search(state.query.text)
            state.searching = false
        }

        LaunchedEffect(state.selectedItem) {
            state.selectedItem?.let {
                weatherData = viewModel.getCurrentWeather(
                    it.id,
                    it.coord.lat.toFloat(),
                    it.coord.lon.toFloat()
                )

                Log.d("MAinACTOVOTU", "-----weatherData: $weatherData")
            }
        }

        if (state.focused) {
            when (state.searchDisplay) {
                SearchDisplay.InitialResults -> {

                }
                SearchDisplay.NoResults -> {

                }

                SearchDisplay.Suggestions -> {

                }

                SearchDisplay.Results -> {
                    SearchResultList(state.searchResults) {
                        state.selectedItem = it
                        state.focused = false
                    }
                }
            }
        } else {
            //display weather
            Weather()
        }
    }
}

@Composable
fun SearchResultList(itemList: List<CityRecord>, onItemClick: (CityRecord) -> Unit) {
    val scrollState = rememberLazyListState()

    LazyColumn(state = scrollState) {
        items(items = itemList, key = { it.id }) {
            CityRow(it, modifier = Modifier
                .padding(12.dp)
                .clickable { onItemClick(it) })
        }
    }
}

@Composable
fun Weather() {

}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    WeatherComposeTheme {
//        WeatherApp()
//    }
//}