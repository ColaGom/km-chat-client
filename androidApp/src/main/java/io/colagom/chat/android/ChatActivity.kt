package io.colagom.chat.android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import io.colagom.ui.ChatScreen

class ChatActivity : ComponentActivity() {
    class Contract : ActivityResultContract<Long, Unit>() {
        override fun createIntent(context: Context, input: Long) =
            Intent(context, ChatActivity::class.java).apply {
                putExtra(NAME_ROOM_ID, input)
            }

        override fun parseResult(resultCode: Int, intent: Intent?) = Unit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val roomId = intent.getLongExtra(NAME_ROOM_ID, -1)


        if (roomId < 0) {
            finish()
            return
        }

        setContent {
            ChatScreen(roomId = roomId)
        }
    }

    companion object {
        private const val NAME_ROOM_ID = "id"
    }
}
