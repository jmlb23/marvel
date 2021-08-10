package com.github.jmlb23.marvel.data

import io.ktor.client.engine.*
import io.ktor.client.engine.ios.*

actual fun engine(): HttpClientEngineFactory<HttpClientEngineConfig> = Ios