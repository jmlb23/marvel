package com.github.jmlb23.marvel.data

import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*

actual fun engine(): HttpClientEngineFactory<HttpClientEngineConfig> = CIO