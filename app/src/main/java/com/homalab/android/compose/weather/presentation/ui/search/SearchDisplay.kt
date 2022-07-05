package com.homalab.android.compose.weather.presentation.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import com.homalab.android.compose.weather.R
import com.homalab.android.compose.weather.domain.entity.City
import com.homalab.android.compose.weather.presentation.components.CityRow
import com.homalab.android.compose.weather.presentation.components.SearchDisplayType
import com.homalab.android.compose.weather.presentation.components.SearchState
import com.homalab.android.compose.weather.util.Dimension4

@Composable
fun SearchDisplay(searchState: SearchState<City>) {
    when (searchState.searchDisplayType) {
        SearchDisplayType.InitialResults -> {

        }
        SearchDisplayType.NoResults -> {

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

        }
        SearchDisplayType.Results -> {
            searchState.searchResults?.let {
                SearchResult(
                    itemList = it
                ) { city ->
                    searchState.selectedItem = city
                    searchState.query = TextFieldValue("")
                    searchState.focused = false
                }
            }
        }
    }
}

@Composable
private fun SearchResult(
    modifier: Modifier = Modifier,
    itemList: List<City>,
    onItemClick: (City) -> Unit
) {
    SearchResultList(modifier, itemList = itemList, onItemClick = onItemClick)
}

@Composable
private fun SearchResultList(
    modifier: Modifier,
    itemList: List<City>,
    onItemClick: (City) -> Unit
) {
    val scrollState = rememberLazyListState()

    LazyColumn(state = scrollState) {
        items(items = itemList, key = { it.id }) {
            CityRow(it, modifier = modifier.clickable { onItemClick(it) })
        }
    }
}