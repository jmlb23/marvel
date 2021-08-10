package com.github.jmlb23.marvel.data.remote

import com.github.jmlb23.marvel.Either
import com.github.jmlb23.marvel.Errors

internal interface CharacterService {
    suspend fun characters(limit: Int, offset: Int): Either<Errors, CharactersResponse>
    suspend fun character(characterId: Int): Either<Errors, CharactersResponse>
}