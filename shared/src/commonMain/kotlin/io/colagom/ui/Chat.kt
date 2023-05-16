package io.colagom.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.colagom.store.ChatAction
import io.colagom.store.ChatStore


@Composable
fun ChatScreen(
    roomId: Long
) {
    Theme {
        Surface {
            val scope = rememberCoroutineScope()
            val store = remember { ChatStore(scope, roomId) }
            val state by store.state.collectAsState()

            Scaffold(
                bottomBar = {
                    ChatInput(
                        joined = state.joined,
                        onClickSend = {
                            store.dispatch(ChatAction.ClickInput(it))
                        }
                    )
                }
            ) {
                LazyColumn(
                    modifier = Modifier.padding(it),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(state.chats) {
                        val bgColor = if (it.isMine) Color.Yellow else Color.LightGray

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = if (it.isMine) Alignment.CenterEnd else Alignment.CenterStart
                        ) {
                            Text(
                                modifier = Modifier
                                    .background(
                                        bgColor,
                                        RoundedCornerShape(8.dp)
                                    ).padding(8.dp),
                                color = Color.Black,
                                text = it.message
                            )
                        }

                    }
                }
            }
        }
    }
}

@Composable
private fun ChatInput(
    joined: Boolean,
    onClickSend: (String) -> Unit
) {
    var input by remember { mutableStateOf("") }
    val hint = if (joined) "Input a new message" else "Input user name"
    val icon = if (joined) Icons.Filled.Send else Icons.Filled.Create

    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text(hint) },
        )
        IconButton(
            onClick = {
                onClickSend(input)
                input = ""
            },
            content = { Icon(icon, null) }
        )
    }
}