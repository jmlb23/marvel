package com.github.jmlb23.marvel.data

import io.ktor.client.engine.*

expect fun engine(): HttpClientEngineFactory<HttpClientEngineConfig>