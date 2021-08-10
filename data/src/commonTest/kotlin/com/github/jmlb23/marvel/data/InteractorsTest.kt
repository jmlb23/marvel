package com.github.jmlb23.marvel.data

import com.github.jmlb23.marvel.Character
import com.github.jmlb23.marvel.Errors
import com.github.jmlb23.marvel.interactors.GetCharactersInteractor
import com.github.jmlb23.marvel.interactors.Interactor
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import kotlin.test.*
import kotlin.time.ExperimentalTime

@ExperimentalTime
class InteractorsTest {
    lateinit var app: KoinApplication

    @BeforeTest
    fun setUp() {
        app = koinApplication {
            modules(DIData)
        }
    }

    @Test
    fun test_get_character() {
        val interactorCharacter = app.koin.get<Interactor<Int, Errors, Character>>(named("character"))
        val id = 1011334
        runBlocking {
            interactorCharacter.invoke(id).fold({
                fail(it.toString())
            }, {
                assertTrue("should be 100") { it.id == id.toLong() }
            })
        }
    }

    @Test
    fun test_get_characters() {
        val interactorCharacters = app.koin.get<GetCharactersInteractor>()
        val page = 0
        runBlocking {
            interactorCharacters.invoke(page).fold({
                fail(it.toString())
            }, {
                assertTrue("should be 100") { it.size == 100 }
            })
        }
    }
}