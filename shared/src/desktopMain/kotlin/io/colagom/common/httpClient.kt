package io.colagom.common

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

actual fun httpClient(): HttpClientEngine {
    return OkHttp.create {
    }
}