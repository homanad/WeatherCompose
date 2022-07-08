package com.homalab.android.compose.weather.presentation.ui.home.search

import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.homalab.android.compose.weather.R
import com.homalab.android.compose.weather.domain.entity.City
import com.homalab.android.compose.weather.presentation.components.MessageText
import com.homalab.android.compose.weather.presentation.ui.home.CityList

@Composable
fun SearchDisplay(searchState: SearchState<City>) {
    when (searchState.searchDisplayType) {
        SearchDisplayType.InitialResults -> {

        }
        SearchDisplayType.NoResults -> {
            MessageText(text = stringResource(id = R.string.search_no_result))
        }
        SearchDisplayType.NetworkUnavailable -> {
            MessageText(text = stringResource(id = R.string.network_unavailable))
        }
        SearchDisplayType.Suggestions -> {
            CityList(
                itemList = searchState.suggestions
            ) { city ->
                searchState.selectedItem = city
                searchState.focused = false
            }
        }
        SearchDisplayType.Results -> {
            searchState.searchResults?.let {
                CityList(
                    itemList = it
                ) { city ->
                    searchState.selectedItem = city
                    searchState.focused = false
                }
            }
        }
    }
}

//@Composable
//private fun SearchResultList(
//    modifier: Modifier = Modifier,
//    itemList: List<City>,
//    onItemClick: (City) -> Unit
//) {
//    val scrollState = rememberLazyListState()
//
//    LazyColumn(state = scrollState, modifier = Modifier.semantics { contentDescription = GlobalConstants.CityListDescription }) {
//        items(items = itemList, key = { it.id }) {
//            CityRow(it, modifier = modifier.clickable { onItemClick(it) })
//        }
//    }
//}

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