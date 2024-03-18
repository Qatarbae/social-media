package com.eltex.androidschool.feature.paging

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ItemProgress(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp))
}

@Preview
@Composable
fun ItemProgressPreview() {
    ItemProgress()
}