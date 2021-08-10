package com.github.jmlb23.marvel.data

import com.github.jmlb23.marvel.Character
import com.github.jmlb23.marvel.Errors
import com.github.jmlb23.marvel.repos.Repository
import kotlinx.coroutines.*
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.koinApplication
import kotlin.test.*
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
class RepoTest {
    lateinit var app: KoinApplication

    @BeforeTest
    fun setUp() {
        app = koinApplication {
            modules(DIData)
        }
    }

    @Test
    fun test_get_characters() {
        val characters = app.koin.get<Repository<Int, Character, Errors>>()
        runBlocking {
            characters.take(0, 100).fold({
                fail(it.toString())
            }, {
                assertTrue("should be 100") { it.size == 100 }
            })
        }
    }
}