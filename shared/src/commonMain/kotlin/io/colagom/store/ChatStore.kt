package io.colagom.store

import io.colagom.chat.model.ChatMessage
import io.colagom.chat.model.ChatRoom
import io.colagom.chat.model.request.ChatType
import io.colagom.chat.model.request.SendChat
import io.colagom.common.Http
import io.colagom.usecase.UseCases
import io.ktor.client.plugins.websocket.ws
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


/**
 * Chatting room state
 *
 * @property room current room
 * @property userName joined user name
 * @property chats chats
 */
data class ChatState(
    val room: ChatRoom? = null,
    val joined: Boolean = false,
    val userName: String = "GUEST",
    val chats: List<ChatMessage> = emptyList()
) : State

/**
 * Chatting room action
 */
sealed interface ChatAction : Action {
    data class ClickInput(val input: String) : ChatAction
}

sealed interface ChatEvent : Event

class ChatStore(
    scope: CoroutineScope,
    private val roomId: Long
) : Store<ChatState, ChatAction, ChatEvent>, CoroutineScope by scope {
    private val _state = MutableStateFlow(ChatState())
    override val state: StateFlow<ChatState> = _state
    private val _event = MutableSharedFlow<ChatEvent>()
    override val event: SharedFlow<ChatEvent> = _event

    private var session: WebSocketSession? = null

    init {
        refresh()

        launch {
            Http.wsClient.ws("chat/$roomId") {
                session = this

                for (frame in incoming) {
                    println("incoming : $frame")

                    (frame as? Frame.Text)?.let { textFrame ->
                        val chat = Json.decodeFromString<ChatMessage>(textFrame.readText())
                        _state.update {
                            it.copy(chats = it.chats + chat)
                        }
                    }
                }
            }
        }
    }

    override fun dispatch(action: ChatAction) {

        when (action) {
            is ChatAction.ClickInput -> {
                val joined = _state.value.joined
                launch {
                    val message = SendChat(
                        type = if (joined) ChatType.MESSAGE else ChatType.JOIN,
                        message = action.input
                    )

                    if (!joined) {
                        _state.update { it.copy(joined = true, userName = action.input) }
                    }

                    session?.send(Json.encodeToString(message))
                }
            }
        }
    }

    private fun refresh() {
        launch {
            _state.update {
                it.copy(room = UseCases.getChatRoom().execute(roomId))
            }
        }
    }
}