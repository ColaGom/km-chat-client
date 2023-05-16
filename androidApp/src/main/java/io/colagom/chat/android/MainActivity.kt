package io.colagom.chat.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.colagom.ui.ChatRoomListScreen

class MainActivity : ComponentActivity() {
    private val chatLauncher = registerForActivityResult(ChatActivity.Contract()) { }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatRoomListScreen(
                onClickRoom = { room ->
                    chatLauncher.launch(room.id)
                }
            )
        }
    }
}
