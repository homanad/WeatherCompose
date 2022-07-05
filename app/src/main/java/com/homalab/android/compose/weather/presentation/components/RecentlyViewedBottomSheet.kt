package com.homalab.android.compose.weather.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.homalab.android.compose.weather.R
import com.homalab.android.compose.weather.domain.entity.City
import com.homalab.android.compose.weather.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecentlyBottomSheetScaffold(
    itemList: List<City>,
    onItemClick: (City) -> Unit,
    modifier: Modifier = Modifier,
    contentBody: @Composable () -> Unit
) {
    val state =
        rememberBottomSheetScaffoldState(bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed))

    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = state,
        sheetShape = BottomSheetShape,
        sheetContent = {
            RecentlyViewedBottomSheetContent(
                itemList,
                onItemClick,
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(MaterialTheme.colorScheme.primaryContainer)
            )
        },
        sheetPeekHeight = RecentlyBottomSheetPeekHeight
    ) {
        contentBody.invoke()
    }
}

@Composable
fun RecentlyViewedBottomSheetContent(
    itemList: List<City>,
    onItemClick: (City) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(
                modifier = Modifier
                    .padding(Dimension1)
                    .height(Dimension1)
                    .width(RecentlyBottomSheetBarWidth)
                    .clip(RoundedCornerShape(Dimension1))
                    .background(Color.Gray),
            )

            Text(
                text = stringResource(id = R.string.recently_viewed),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = Dimension1)
            )

            if (itemList.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.no_history),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(Dimension2)
                )
            } else {
                LazyColumn {
                    items(items = itemList, key = { it.id }) {
                        CityRow(it, modifier = Modifier.clickable { onItemClick(it) })
                    }
                }
            }
        }
    }
}

val BottomSheetShape = RoundedCornerShape(
    topStart = RecentlyBottomSheetCorner,
    topEnd = RecentlyBottomSheetCorner,
    bottomStart = Dimension0,
    bottomEnd = Dimension0
)