package com.github.jmlb23.marvel.data.remote.impl

import com.github.jmlb23.marvel.*
import com.github.jmlb23.marvel.Either.Companion.toEither
import com.github.jmlb23.marvel.data.remote.CharacterService
import com.github.jmlb23.marvel.data.remote.CharactersResponse
import com.github.jmlb23.marvel.data.withParams
import io.ktor.client.*
import io.ktor.client.request.*

internal class CharacterServiceImpl(private val client: HttpClient) : CharacterService {
    override suspend fun characters(limit: Int, offset: Int): Either<Errors.ApiError, CharactersResponse> {
        return client
            .runCatching {
                get<CharactersResponse>(
                    "${BuildKonfig.URL_BASE}/characters".withParams(
                        mapOf(
                            "limit" to "$limit",
                            "offset" to "$offset"
                        )
                    )
                )
            }
            .toEither().mapError { Errors.ApiError(it.message.orEmpty()) }
    }

    override suspend fun character(characterId: Int): Either<Errors.ApiError, CharactersResponse> {
        return client
            .runCatching { get<CharactersResponse>("${BuildKonfig.URL_BASE}/characters/$characterId".withParams()) }
            .toEither().mapError { Errors.ApiError(it.message.orEmpty()) }
    }
}