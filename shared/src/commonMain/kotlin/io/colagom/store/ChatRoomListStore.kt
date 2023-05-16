package io.colagom.store

import io.colagom.chat.dto.ChatRoom
import io.colagom.chat.dto.request.CreateChatRoom
import io.colagom.usecase.CreateChatRoomUseCase
import io.colagom.usecase.GetAllChatRoomUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


data class ChatRoomListState(
    val rooms: List<ChatRoom> = emptyList()
) : State

sealed interface ChatRoomListAction : Action {
    data class CreateRoom(val name: String) : ChatRoomListAction
}

sealed interface ChatRoomListEvent : Event {

    data class CreateRoomSuccess(
        val room: ChatRoom
    ) : ChatRoomListEvent

    data class CreateRoomFailure(
        val reason: String
    ) : ChatRoomListEvent
}

class ChatRoomListStore(
    scope: CoroutineScope
) : Store<ChatRoomListState, ChatRoomListAction, ChatRoomListEvent>, CoroutineScope by scope {
    private val _state = MutableStateFlow(ChatRoomListState())
    override val state: StateFlow<ChatRoomListState> = _state
    private val _event = MutableSharedFlow<ChatRoomListEvent>()
    override val event: SharedFlow<ChatRoomListEvent> = _event

    init {
        launch {
            while (true) {
                refresh()
                delay(5.seconds)
            }
        }
    }

    private fun refresh() {
        launch {
            _state.update {
                it.copy(rooms = GetAllChatRoomUseCase().execute(Unit))
            }
        }
    }

    override fun dispatch(action: ChatRoomListAction) {
        launch {
            when (action) {
                is ChatRoomListAction.CreateRoom -> {
                    if (action.name.isBlank()) {
                        _event.emit(ChatRoomListEvent.CreateRoomFailure("Room name is blank"))
                        return@launch
                    }

                    val event = runCatching {
                        val room = CreateChatRoomUseCase().execute(CreateChatRoom(action.name, 10))
                        ChatRoomListEvent.CreateRoomSuccess(room)
                    }.getOrElse {
                        it.printStackTrace()
                        ChatRoomListEvent.CreateRoomFailure("Failed request to create room")
                    }

                    if (event is ChatRoomListEvent.CreateRoomSuccess) {
                        refresh()
                    }

                    _event.emit(event)
                }
            }
        }
    }
}