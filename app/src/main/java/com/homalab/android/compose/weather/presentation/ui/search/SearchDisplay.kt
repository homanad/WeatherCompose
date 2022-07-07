package com.homalab.android.compose.weather.presentation.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import com.homalab.android.compose.constants.GlobalConstants
import com.homalab.android.compose.weather.R
import com.homalab.android.compose.weather.domain.entity.City
import com.homalab.android.compose.weather.presentation.components.CityRow
import com.homalab.android.compose.weather.util.Dimension4

@Composable
fun SearchDisplay(searchState: SearchState<City>) {
    when (searchState.searchDisplayType) {
        SearchDisplayType.InitialResults -> {

        }
        SearchDisplayType.NoResults -> {
            Text(
                text = stringResource(id = R.string.search_no_result),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimension4),
                textAlign = TextAlign.Center
            )
        }
        SearchDisplayType.NetworkUnavailable -> {
            Text(
                text = stringResource(id = R.string.network_unavailable),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimension4),
                textAlign = TextAlign.Center
            )
        }
        SearchDisplayType.Suggestions -> {
            SearchResultList(
                itemList = searchState.suggestions
            ) { city ->
                searchState.selectedItem = city
                searchState.focused = false
            }
        }
        SearchDisplayType.Results -> {
            searchState.searchResults?.let {
                SearchResultList(
                    itemList = it
                ) { city ->
                    searchState.selectedItem = city
                    searchState.focused = false
                }
            }
        }
    }
}

@Composable
private fun SearchResultList(
    modifier: Modifier = Modifier,
    itemList: List<City>,
    onItemClick: (City) -> Unit
) {
    val scrollState = rememberLazyListState()

    LazyColumn(state = scrollState, modifier = Modifier.semantics { contentDescription = GlobalConstants.CityListDescription }) {
        items(items = itemList, key = { it.id }) {
            CityRow(it, modifier = modifier.clickable { onItemClick(it) })
        }
    }
}

@Composable
fun <T> rememberSearchState(
    query: TextFieldValue = TextFieldValue(""),
    focused: Boolean = false,
    searching: Boolean = false,
    suggestions: List<T> = emptyList(),
    searchResults: List<T> = emptyList(),
    selectedItem: T? = null
): SearchState<T> {
    return remember {
        SearchState(
            query = query,
            focused = focused,
            searching = searching,
            suggestions = suggestions,
            searchResults = searchResults,
            selectedItem = selectedItem
        )
    }
}

@Stable
class SearchState<T>(
    query: TextFieldValue,
    focused: Boolean,
    searching: Boolean,
    suggestions: List<T>,
    searchResults: List<T>?,
    selectedItem: T?
) {
    var query by mutableStateOf(query)
    var focused by mutableStateOf(focused)
    var searching by mutableStateOf(searching)
    var suggestions by mutableStateOf(suggestions)
    var searchResults by mutableStateOf(searchResults)
    var selectedItem by mutableStateOf(selectedItem)

    val searchDisplayType: SearchDisplayType
        get() = when {
            !focused && query.text.isEmpty() -> SearchDisplayType.InitialResults
            focused && query.text.isEmpty() -> SearchDisplayType.Suggestions
            searchResults?.isEmpty() == true -> SearchDisplayType.NoResults
            searchResults == null -> SearchDisplayType.NetworkUnavailable
            else -> SearchDisplayType.Results
        }
}

enum class SearchDisplayType {
    InitialResults, Suggestions, Results, NoResults, NetworkUnavailable
}