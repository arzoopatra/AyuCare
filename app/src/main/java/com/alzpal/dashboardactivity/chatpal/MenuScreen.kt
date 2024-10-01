package com.alzpal.dashboardactivity.chatpal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.alzpal.R


data class MenuItem(
    val routeId: String,
    val titleResId: Int,
    val descriptionResId: Int
)

@Composable
fun MenuScreen(
    onItemClicked: (String) -> Unit = { }
) {
    val menuItems = listOf(
        MenuItem("photo_reasoning", R.string.menu_reason_title, R.string.menu_reason_description),
        MenuItem("chat", R.string.menu_chat_title, R.string.menu_chat_description)
    )

    LazyColumn(
        Modifier
            .padding(top = 16.dp, bottom = 16.dp)
    ) {
        items(menuItems) { menuItem ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8C0F0)), // Lavender color
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 7.dp),
            ) {
                Column(
                    modifier = Modifier
                        .padding(all = 11.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(menuItem.titleResId),
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp) // Increased font size
                    )
                    Text(
                        text = stringResource(menuItem.descriptionResId),
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp), // Increased font size
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    TextButton(
                        onClick = {
                            onItemClicked(menuItem.routeId)
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(text = stringResource(R.string.action_try))
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun MenuScreenPreview() {
    MenuScreen()
}
