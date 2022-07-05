package com.homalab.android.compose.weather.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import com.homalab.android.compose.weather.util.*

@Composable
fun SearchBar(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    onSearchFocusChange: (Boolean) -> Unit,
    onClearQuery: () -> Unit,
    onBack: () -> Unit,
    searching: Boolean,
    focused: Boolean,
    modifier: Modifier = Modifier
) {

    val focusManager = LocalFocusManager.current
//    val keyboardController = LocalSoftwareKeyboardController.current
    if (!focused) focusManager.clearFocus()

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AnimatedVisibility(visible = focused) {
            // Back button
            IconButton(
                modifier = Modifier.padding(start = IconPadding),
                onClick = {
//                    focusManager.clearFocus()
//                    keyboardController?.hide()
                    onBack()
                }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        }

        SearchTextField(
            query,
            onQueryChange,
            onSearchFocusChange,
            onClearQuery,
            searching,
            focused,
            modifier.weight(1f)
        )
    }
}

@Composable
fun SearchTextField(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    onSearchFocusChange: (Boolean) -> Unit,
    onClearQuery: () -> Unit,
    searching: Boolean,
    focused: Boolean,
    modifier: Modifier = Modifier
) {

    val focusRequester = remember { FocusRequester() }

    Surface(
        modifier = modifier
            .then(
                Modifier
                    .height(SearchBarHeight)
                    .padding(
                        top = Dimension2,
                        bottom = Dimension2,
                        start = if (!focused) Dimension4 else Dimension0,
                        end = Dimension2
                    )
            ),
        color = Color(0xffF5F5F5),
        shape = RoundedCornerShape(percent = 50),
    ) {

        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.primaryContainer) {
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = modifier
            ) {

                if (query.text.isEmpty()) {
                    SearchHint(modifier.padding(start = Dimension6, end = Dimension2))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    BasicTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .onFocusChanged {
                                onSearchFocusChange(it.isFocused)
                            }
                            .focusRequester(focusRequester)
                            .padding(
                                top = Dimension2,
                                bottom = Dimension2,
                                start = Dimension6,
                                end = Dimension2
                            ),
                        singleLine = true
                    )

                    when {
                        searching -> {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(horizontal = Dimension2)
                                    .size(Dimension9)
                            )
                        }
                        query.text.isNotEmpty() -> {
                            IconButton(onClick = onClearQuery) {
                                Icon(imageVector = Icons.Filled.Close, contentDescription = null)
                            }
                        }
                    }
                }
            }
        }

    }
}

@Composable
private fun SearchHint(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)

    ) {
        Text(
            color = Color(0xff757575),
            text = "Search a City",
        )
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

    val searchDisplay: SearchDisplay
        get() = when {
            !focused && query.text.isEmpty() -> SearchDisplay.InitialResults
            focused && query.text.isEmpty() -> SearchDisplay.Suggestions
            searchResults?.isEmpty() == true -> SearchDisplay.NoResults
            searchResults == null -> SearchDisplay.NetworkUnavailable
            else -> SearchDisplay.Results
        }
}

enum class SearchDisplay {
    InitialResults, Suggestions, Results, NoResults, NetworkUnavailable
}