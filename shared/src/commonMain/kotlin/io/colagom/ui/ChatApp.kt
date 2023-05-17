package io.colagom.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.colagom.chat.model.ChatRoom


/**
 * It's include whole application UI.
 *
 */
@Composable
fun ChatApp() {
    var selectedRoom by remember {
        mutableStateOf<ChatRoom?>(null)
    }

    Theme {
        Surface {
            Column(modifier = Modifier.fillMaxSize()) {
                val room = selectedRoom
                if (room != null) {
                    ChatScreen(room.id)
                    DisposableEffect(room) {
                        onDispose {
                            selectedRoom = null
                        }
                    }
                } else {
                    ChatRoomListScreen {
                        selectedRoom = it
                    }
                }
            }
        }
    }
}