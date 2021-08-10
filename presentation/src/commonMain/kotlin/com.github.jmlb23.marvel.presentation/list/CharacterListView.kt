package com.github.jmlb23.marvel.presentation.list

import com.github.jmlb23.marvel.presentation.IView
import com.github.jmlb23.marvel.Character
import com.github.jmlb23.marvel.Either
import com.github.jmlb23.marvel.Errors
import com.github.jmlb23.marvel.interactors.GetCharactersInteractor
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class CharacterListView(private val interactor: GetCharactersInteractor) :
    IView<Errors, List<Character>> {
    private val scope = CoroutineScope(Dispatchers.Main)
    private val state = MutableStateFlow<Either<Errors, List<Character>>>(Either.fromRight(emptyList()))

    override fun start(onError: (Errors) -> Unit, onRender: (List<Character>) -> Unit) {
        state.filter {
            when (it) {
                is Either.Left -> true
                is Either.Right -> it.right.isNotEmpty()
            }
        }.onEach { it.fold(onError, onRender) }.launchIn(scope)
    }

    @Throws(Throwable::class)
    suspend fun getList(page: Int) {
        interactor(page).fold({
            state.tryEmit(Either.fromLeft(it))
        }, {
            state.tryEmit(Either.fromRight(it))
        })
    }

    override fun dispose() = scope.cancel()
}