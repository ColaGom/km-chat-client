package io.colagom.usecase

import io.colagom.chat.dto.ChatRoom
import io.colagom.common.Http
import io.ktor.client.call.body
import io.ktor.client.request.get

class GetAllChatRoomUseCase : VoidUseCase<List<ChatRoom>> {
    override suspend fun execute(input: Unit): List<ChatRoom> = Http.client.get("room").body()
}
