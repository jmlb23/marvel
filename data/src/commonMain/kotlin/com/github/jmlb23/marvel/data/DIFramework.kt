package com.github.jmlb23.marvel.data

import com.github.jmlb23.marvel.Character
import com.github.jmlb23.marvel.Errors
import com.github.jmlb23.marvel.data.remote.impl.CharacterServiceImpl
import com.github.jmlb23.marvel.data.remote.CharacterService
import com.github.jmlb23.marvel.data.repo.CharacterRepoImpl
import com.github.jmlb23.marvel.interactors.GetCharacterInteractor
import com.github.jmlb23.marvel.interactors.GetCharactersInteractor
import com.github.jmlb23.marvel.interactors.Interactor
import com.github.jmlb23.marvel.repos.Repository
import io.github.reactivecircus.cache4k.Cache
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import org.koin.core.qualifier.named
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import kotlin.reflect.KClass
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

@ExperimentalTime
val DIData = module {
    single {
        HttpClient(engine()) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            developmentMode = true
        }
    }
    single<CharacterService> { CharacterServiceImpl(get()) }
    single<Repository<Int, Character, Errors>> { CharacterRepoImpl(get(), get()) }
    single { GetCharactersInteractor(get()) }
    single<Interactor<Int, Errors, Character>>(named("character")) { GetCharacterInteractor(get()) }
    single<Cache<Long, Character>> { Cache.Builder().expireAfterAccess(24.toDuration(DurationUnit.MINUTES)).build() }

}