package io.colagom.common

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

actual fun httpClient(): HttpClientEngine {
    return Darwin.create {  }
}