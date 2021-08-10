package com.github.jmlb23.marvel.presentation

interface IView<E, R> {
    fun start(onError: (E) -> Unit, onRender: (R) -> Unit)
    fun dispose()
}