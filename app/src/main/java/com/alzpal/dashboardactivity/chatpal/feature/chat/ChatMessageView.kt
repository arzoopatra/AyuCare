package com.alzpal.dashboardactivity.chatpal.feature.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ChatMessageView(chatMessage: ChatMessage) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "ID: ${chatMessage.id}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Text: ${chatMessage.text}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Participant: ${chatMessage.participant}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Pending: ${chatMessage.isPending}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewChatMessageView() {
    ChatMessageView(
        chatMessage = ChatMessage(
            text = "Hello, This is a Test Message!",
            participant = Participant.MODEL,
            isPending = false
        )
    )
}
