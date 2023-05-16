package io.colagom.usecase

import io.colagom.chat.dto.ChatRoom
import io.colagom.common.Http
import io.ktor.client.call.body
import io.ktor.client.request.get

class GetChatRoomUseCase : UseCase<Long, ChatRoom> {
    override suspend fun execute(input: Long): ChatRoom =
        Http.client.get("room/$input").body()
}