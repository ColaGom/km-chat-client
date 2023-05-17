package io.colagom.usecase

import io.colagom.chat.model.ChatRoom
import io.colagom.chat.usecase.GetAllChatRoomUseCase
import io.colagom.common.Http
import io.ktor.client.call.body
import io.ktor.client.request.get

class GetAllChatRoomUseCaseImpl : GetAllChatRoomUseCase {
    override suspend fun execute(input: Unit): List<ChatRoom> = Http.client.get("room").body()
}
