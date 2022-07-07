package com.homalab.android.compose.weather.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.homalab.android.compose.weather.util.Dimension4

@Composable
fun MessageText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center
) {
    Text(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimension4),
        textAlign = textAlign
    )
}