package io.colagom.usecase

import io.colagom.chat.usecase.CreateChatRoomUseCase
import io.colagom.chat.usecase.GetAllChatRoomUseCase
import io.colagom.chat.usecase.GetChatRoomUseCase

object UseCases {
    fun getChatRoom(): GetChatRoomUseCase = GetChatRoomUseCaseImpl()
    fun createChatRoom(): CreateChatRoomUseCase = CreateChatRoomUseCaseImpl()
    fun getAllChatRoom(): GetAllChatRoomUseCase = GetAllChatRoomUseCaseImpl()
}