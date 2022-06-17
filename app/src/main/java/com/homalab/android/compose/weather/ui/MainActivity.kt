package com.homalab.android.compose.weather.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.homalab.android.compose.weather.ui.components.SearchBar
import com.homalab.android.compose.weather.ui.components.SearchDisplay
import com.homalab.android.compose.weather.ui.components.SearchState
import com.homalab.android.compose.weather.ui.components.rememberSearchState
import com.homalab.android.compose.weather.ui.model.City
import com.homalab.android.compose.weather.ui.theme.WeatherComposeTheme
import kotlinx.coroutines.delay

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
private fun WeatherApp(state: SearchState<City> = rememberSearchState()) {
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
//        state.searchResults = //todo
            state.searching = false
        }

        when (state.searchDisplay) {
            SearchDisplay.InitialResults -> {

            }
            SearchDisplay.NoResults -> {

            }

            SearchDisplay.Suggestions -> {

            }

            SearchDisplay.Results -> {

            }
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