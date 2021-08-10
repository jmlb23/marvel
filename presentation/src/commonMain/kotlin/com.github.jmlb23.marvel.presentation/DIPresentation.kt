package com.github.jmlb23.marvel.presentation

import com.github.jmlb23.marvel.presentation.detail.CharacterView
import com.github.jmlb23.marvel.presentation.list.CharacterListView
import org.koin.core.qualifier.named
import org.koin.dsl.module

val DIPresentation = module {
    factory { CharacterListView(get()) }
    factory { CharacterView(get(named("character"))) }
}