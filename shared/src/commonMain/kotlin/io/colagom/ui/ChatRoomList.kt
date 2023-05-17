package io.colagom.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import io.colagom.chat.model.ChatRoom
import io.colagom.common.PLATFORM
import io.colagom.store.ChatRoomListAction
import io.colagom.store.ChatRoomListEvent
import io.colagom.store.ChatRoomListStore
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun ChatRoomListScreen(
    onClickRoom: (ChatRoom) -> Unit
) {
    Theme {
        Surface {
            var roomName by remember { mutableStateOf("") }
            var visible by remember { mutableStateOf(false) }
            val scaffoldState = rememberScaffoldState()

            Scaffold(
                scaffoldState = scaffoldState,
                topBar = {
                    TopAppBar(title = {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Text(modifier = Modifier.align(Alignment.Center), text = "KM - Chat($PLATFORM)")
                            IconButton(
                                modifier = Modifier.align(Alignment.CenterEnd),
                                onClick = { visible = true },
                                content = { Icon(Icons.Filled.Add, null) }
                            )
                        }
                    })
                }
            ) {
                val scope = rememberCoroutineScope()
                val store = remember { ChatRoomListStore(scope) }
                val state by store.state.collectAsState()

                LaunchedEffect(store) {
                    store.event.onEach { event ->
                        when (event) {
                            is ChatRoomListEvent.CreateRoomFailure -> {
                                scaffoldState.snackbarHostState.showSnackbar(event.reason)
                            }

                            is ChatRoomListEvent.CreateRoomSuccess -> {
                                scaffoldState.snackbarHostState.showSnackbar("Success to create room = ${event.room.name}")
                            }
                        }
                    }.launchIn(this)
                }

                LazyColumn(
                    modifier = Modifier.padding(it)
                ) {
                    items(state.rooms) {
                        ChatRoomRow(onClick = {
                            onClickRoom(it)
                        }, item = it)
                    }
                }

                AnimatedVisibility(visible = visible) {
                    CreateRoomBox(
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = .7f))
                            .padding(it)
                            .fillMaxSize(),
                        roomName = roomName,
                        onRoomNameChange = { roomName = it },
                        onClickCreate = {
                            visible = false
                            store.dispatch(ChatRoomListAction.CreateRoom(roomName))
                        },
                        onDismissRequest = {
                            visible = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CreateRoomBox(
    modifier: Modifier,
    roomName: String,
    onRoomNameChange: (String) -> Unit,
    onClickCreate: () -> Unit,
    onDismissRequest: () -> Unit
) {
    Box(
        modifier = Modifier.clickable { onDismissRequest() }.then(modifier),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                roomName,
                onValueChange = onRoomNameChange,
                placeholder = { Text("Input room name") }
            )

            IconButton(onClick = onClickCreate) {
                Icon(Icons.Filled.Create, null)
            }
        }
    }
}

@Composable
private fun ChatRoomRow(
    onClick: () -> Unit,
    item: ChatRoom
) {
    Row(
        modifier = Modifier.clickable { onClick() }.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(item.name)
        Text("${item.users.size} / ${item.limit}")
    }
}