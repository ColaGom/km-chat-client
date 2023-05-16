package io.colagom.chat

import androidx.compose.ui.window.ComposeUIViewController
import io.colagom.chat.dto.ChatRoom
import io.colagom.ui.ChatRoomListScreen
import io.colagom.ui.ChatScreen
import platform.UIKit.UIViewController


fun ChatRoomListController(
    onClickRoom: (room: ChatRoom) -> Unit
): UIViewController = ComposeUIViewController {
    ChatRoomListScreen(
        onClickRoom = { room ->
            onClickRoom(room)
        }
    )
}

fun ChatController(roomId: Long): UIViewController = ComposeUIViewController {
    ChatScreen(roomId)
}