package com.homalab.android.compose.weather.presentation.components

import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppBottomSheetScaffold(
    modifier: Modifier = Modifier,
    scaffoldState: BottomSheetScaffoldState,
    sheetShape: Shape,
    sheetPeekHeight: Dp,
    sheetContent: @Composable () -> Unit,
    contentBody: @Composable () -> Unit
) {
    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        sheetShape = sheetShape,
        sheetContent = { sheetContent.invoke() },
        sheetPeekHeight = sheetPeekHeight
    ) {
        contentBody.invoke()
    }
}