package com.github.jmlb23.marvel.repos

import com.github.jmlb23.marvel.Character
import com.github.jmlb23.marvel.Either
import com.github.jmlb23.marvel.Errors

interface Repository<ID, D, E: Errors> {
    suspend fun get(by: ID): Either<E, D>
    suspend fun remove(by: ID): Either<E, D>
    suspend fun add(by: D): Either<E, ID>
    suspend fun update(on: ID, new: D): Either<E, D>
    suspend fun filter(where: (D) -> Boolean): Either<E, List<D>>
    suspend fun take(offset: Int, limit: Int): Either<E, List<D>>
}

interface CharacterRepository : Repository<Int,Character, Errors>