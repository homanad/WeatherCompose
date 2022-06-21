package com.homalab.android.compose.weather.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.homalab.android.compose.weather.domain.entity.WeatherData
import com.homalab.android.compose.weather.ui.components.*
import com.homalab.android.compose.weather.ui.model.CityRecord
import com.homalab.android.compose.weather.ui.model.search
import com.homalab.android.compose.weather.ui.theme.WeatherComposeTheme
import com.homalab.android.compose.weather.util.Constants.CONDITION_PATTERN
import com.homalab.android.compose.weather.util.Constants.C_DEGREE_MIN_MAX_PATTERN
import com.homalab.android.compose.weather.util.Constants.C_DEGREE_PATTERN
import com.homalab.android.compose.weather.util.Constants.OPEN_WEATHER_ICON_URL_PATTERN
import com.homalab.android.compose.weather.util.TimeFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@ExperimentalMaterial3Api
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

@ExperimentalMaterial3Api
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
            if (weatherData != null) {
                WeatherDisplay(weatherData!!)
            } else {

            }
        }
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

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = weatherData.name, style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = TimeFormatter.formatFullTime(weatherData.dt, weatherData.timeZone))

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = C_DEGREE_PATTERN.format(weatherData.main.temp),
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = rememberAsyncImagePainter(OPEN_WEATHER_ICON_URL_PATTERN.format(weatherData.weather[0].icon)),
            contentDescription = null,
            modifier = Modifier.size(128.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = CONDITION_PATTERN.format(
                weatherData.weather[0].main,
                weatherData.weather[0].description
            ),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = C_DEGREE_MIN_MAX_PATTERN.format(
                weatherData.main.temp_min,
                weatherData.main.temp_max
            ), style = MaterialTheme.typography.titleMedium
        )


        Spacer(modifier = Modifier.height(8.dp))

        val cardModifier = Modifier.padding(8.dp)

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
                description = weatherData.wind.speed.toString() + "m/s - " + weatherData.wind.deg
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

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    WeatherComposeTheme {
//        WeatherApp()
//    }
//}