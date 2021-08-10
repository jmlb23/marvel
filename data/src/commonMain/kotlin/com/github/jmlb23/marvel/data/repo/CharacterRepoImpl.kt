package com.github.jmlb23.marvel.data.repo

import com.github.jmlb23.marvel.Character
import com.github.jmlb23.marvel.Either
import com.github.jmlb23.marvel.Either.Companion.flatMap
import com.github.jmlb23.marvel.Errors
import com.github.jmlb23.marvel.data.remote.CharacterService
import com.github.jmlb23.marvel.data.toDomain
import com.github.jmlb23.marvel.repos.CharacterRepository
import io.github.reactivecircus.cache4k.Cache

internal class CharacterRepoImpl(
    private val cache: Cache<Long, Character>,
    private val network: CharacterService
) :
    CharacterRepository {
    override suspend fun get(by: Int): Either<Errors, Character> =
        cache.get(by.toLong())?.let {
            Either.fromRight(it)
        } ?: network.character(by).flatMap {
            it.data?.results?.firstOrNull { it.id == by.toLong() }?.toDomain()?.let {
                cache.put(it.id ?: 0, it)
                Either.fromRight(it)
            } ?: Either.fromLeft(Errors.ApiError("Not Found"))
        }

    override suspend fun remove(by: Int): Either<Errors, Character> = Either.fromLeft(Errors.NotSupported)

    override suspend fun add(by: Character): Either<Errors, Int> = Either.fromLeft(Errors.NotSupported)

    override suspend fun update(on: Int, new: Character): Either<Errors, Character> =
        Either.fromLeft(Errors.NotSupported)

    override suspend fun filter(where: (Character) -> Boolean): Either<Errors, List<Character>> =
        cache.asMap().values.filter(where).takeIf { it.isNotEmpty() }?.let {
            Either.fromRight(it)
        } ?: network.characters(100, 0)
            .map {
                it.data?.results?.map { it.toDomain() }.orEmpty().onEach { cache.put(it.id ?: 0, it) }.filter(where)
            }

    override suspend fun take(offset: Int, limit: Int): Either<Errors, List<Character>> =
         network.characters(limit, offset)
            .map { (cache.asMap().values.toList() + it.data?.results?.map { it.toDomain() }.orEmpty()).onEach { cache.put(it.id ?: 0, it) } }

}