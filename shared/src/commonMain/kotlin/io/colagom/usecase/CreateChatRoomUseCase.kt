package io.colagom.usecase

import io.colagom.chat.dto.ChatRoom
import io.colagom.chat.dto.request.CreateChatRoom
import io.colagom.common.Http
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class CreateChatRoomUseCase : UseCase<CreateChatRoom, ChatRoom> {
    override suspend fun execute(input: CreateChatRoom): ChatRoom =
        Http.client.post("room") {
            contentType(ContentType.Application.Json)
            setBody(input)
        }.body()
}