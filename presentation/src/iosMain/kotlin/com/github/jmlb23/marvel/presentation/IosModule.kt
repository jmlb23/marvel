package com.github.jmlb23.marvel.presentation

import com.github.jmlb23.marvel.data.DIData
import com.github.jmlb23.marvel.presentation.detail.CharacterView
import com.github.jmlb23.marvel.presentation.list.CharacterListView
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.time.ExperimentalTime

val module = module {
    factory { CharacterListView(get()) }
    factory { CharacterView(get(named("character"))) }
}

@ExperimentalTime
fun initIosDependencies() = startKoin {
    modules(module, DIData)
}

class IosComponent : KoinComponent {
    fun provideCharacterView(): CharacterView = get()
    fun provideCharacterListView(): CharacterListView = get()
}