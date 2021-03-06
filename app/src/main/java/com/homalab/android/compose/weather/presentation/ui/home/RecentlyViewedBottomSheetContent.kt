package com.homalab.android.compose.weather.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.homalab.android.compose.constants.GlobalConstants
import com.homalab.android.compose.weather.R
import com.homalab.android.compose.weather.domain.entity.City
import com.homalab.android.compose.weather.presentation.components.RoundedLine
import com.homalab.android.compose.weather.util.*

@Composable
fun RecentlyViewedBottomSheetContent(
    itemList: List<City>,
    onItemClick: (City) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.semantics {
        contentDescription = GlobalConstants.BottomSheetDescription
    }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            RoundedLine()

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
                CityList(itemList = itemList, onItemClick = { onItemClick(it) })
            }
        }
    }
}

val RecentlyViewedBottomSheetShape = RoundedCornerShape(
    topStart = RecentlyBottomSheetCorner,
    topEnd = RecentlyBottomSheetCorner,
    bottomStart = Dimension0,
    bottomEnd = Dimension0
)