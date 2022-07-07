package com.homalab.android.compose.weather.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.homalab.android.compose.constants.GlobalConstants
import com.homalab.android.compose.weather.domain.entity.City
import com.homalab.android.compose.weather.util.Dimension3
import com.homalab.android.compose.weather.util.SpacerLineSize

@Composable
fun CityList(
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
private fun CityRow(city: City, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = city.name, modifier = Modifier.padding(Dimension3))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(SpacerLineSize)
                .background(Color.Gray.copy(alpha = 0.5f))
        )
    }
}