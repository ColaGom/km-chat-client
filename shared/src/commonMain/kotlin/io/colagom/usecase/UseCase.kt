package io.colagom.usecase

interface UseCase<INPUT, OUTPUT> {
    suspend fun execute(input: INPUT): OUTPUT
}

typealias VoidUseCase<OUTPUT> = UseCase<Unit, OUTPUT>