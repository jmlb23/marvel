package com.github.jmlb23.marvel.presentation.detail

import com.github.jmlb23.marvel.presentation.IView
import com.github.jmlb23.marvel.Character
import com.github.jmlb23.marvel.Either
import com.github.jmlb23.marvel.Errors
import com.github.jmlb23.marvel.interactors.Interactor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*

class CharacterView(private val interactor: Interactor<Int, Errors, Character>) : IView<Errors, Character> {
    private val state = MutableStateFlow<Either<Errors, Character?>>(Either.fromRight(null))
    private val scope = CoroutineScope(Dispatchers.Main)

    override fun dispose() =
        scope.cancel()

    suspend fun getDetail(id: Int) {
        interactor(id).fold({
            state.tryEmit(Either.fromLeft(it))
        }, {
            state.tryEmit(Either.fromRight(it))
        })
    }

    override fun start(onError: (Errors) -> Unit, onRender: (Character) -> Unit) {
        state.onEach { it.fold(onError) { it?.let { onRender(it) } } }.launchIn(scope)
    }
}