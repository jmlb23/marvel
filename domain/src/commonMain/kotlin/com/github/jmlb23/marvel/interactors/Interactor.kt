package com.github.jmlb23.marvel.interactors

import com.github.jmlb23.marvel.*
import com.github.jmlb23.marvel.repos.Repository

interface Interactor<in I, out E : Errors, out O> {
    suspend operator fun invoke(input: I): Either<E, O>
}


class GetCharactersInteractor(val repo: Repository<Int, Character, Errors>) :
    Interactor<Int, Errors, List<Character>> {
    override suspend fun invoke(input: Int): Either<Errors, List<Character>> {
        val (limit, offset) = input.fromPage()
        println("Limit: $limit Offset: $offset")
        return repo.take(limit = limit, offset = offset)
    }

}

class GetCharacterInteractor(val repo: Repository<Int, Character, Errors>) :
    Interactor<Int, Errors, Character> {
    override suspend fun invoke(input: Int): Either<Errors, Character> {
        return repo.get(input)
    }
}