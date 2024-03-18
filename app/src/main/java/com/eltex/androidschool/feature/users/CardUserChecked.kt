package com.eltex.androidschool.feature.users

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.eltex.androidschool.model.UserUiModel
import com.eltex.androidschool.theme.ComposeAppTheme

@Composable
fun CardUserChecked(
    user: UserUiModel,
    checkedListener: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    checkable: Boolean = false,
) {
    Card(modifier.fillMaxWidth()) {
        Column {
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(16.dp))

                Box(
                    Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        color = MaterialTheme.colorScheme.onPrimary,
                        text = user.name.firstOrNull()?.uppercase().orEmpty()
                    )
                    AsyncImage(
                        modifier = Modifier
                            .size(40.dp),
                        model = user.avatar,
                        contentDescription = null,
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(Modifier.weight(1F)) {
                    Text(text = user.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = user.login, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                if (checkable) {
                    Checkbox(
                        checked = user.checked,
                        onCheckedChange = checkedListener,
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Preview
@Composable
fun CardUserCheckedPreview() {
    CardUserChecked(
        user = UserUiModel(
            login = "jgummera8",
            name = "Adison Levin",
        ),
        checkedListener = {},
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CardUserCheckedPreviewDark() {
    ComposeAppTheme {
        CardUserChecked(
            user = UserUiModel(
                login = "jgummera8",
                name = "Adison Levin",
            ),
            checkedListener = {},
        )
    }
}
