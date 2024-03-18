package com.eltex.androidschool.feature.paging

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eltex.androidschool.R
import com.eltex.androidschool.theme.ComposeAppTheme
import com.eltex.androidschool.util.getText

@Composable
fun ItemError(error: Throwable, modifier: Modifier = Modifier, onRetryClick: () -> Unit) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = error.getText(LocalContext.current))

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onRetryClick) {
            Text(text = stringResource(id = R.string.retry))
        }
    }
}

@Preview
@Composable
fun ItemErrorPreview() {
    ComposeAppTheme {
        ItemError(RuntimeException("Test")) {}
    }
}