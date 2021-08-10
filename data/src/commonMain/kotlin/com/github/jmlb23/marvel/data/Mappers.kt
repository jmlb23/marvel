package com.github.jmlb23.marvel.data

import com.github.jmlb23.marvel.*
import com.github.jmlb23.marvel.data.remote.*
import com.github.jmlb23.marvel.data.remote.ApiCharacter
import com.github.jmlb23.marvel.data.remote.ApiComics
import com.github.jmlb23.marvel.data.remote.ApiComicsItem
import com.github.jmlb23.marvel.data.remote.ApiThumbnail
import com.github.jmlb23.marvel.data.remote.ApiURL

interface Mapper<in I, out O> {
    fun mapper(input: I): O
}

internal class ApiResourceMapper : Mapper<ApiCharacter, Character> {
    override fun mapper(input: ApiCharacter): Character =
        Character(
            id = input.id,
            name = input.name,
            description = input.description,
            modified = input.modified,
            thumbnail = input.thumbnail?.toDomain(),
            resourceURI = input.resourceURI,
            comics = input.comics?.toDomain(),
            series = input.series?.toDomain(),
            stories = input.stories?.toDomain(),
            events = input.events?.toDomain(),
            urls = input.urls?.map { it.toDomain() }
        )

}

internal class ResourceMapper : Mapper<Character, ApiCharacter> {
    override fun mapper(input: Character): ApiCharacter =
        ApiCharacter(
            id = input.id,
            name = input.name,
            description = input.description,
            modified = input.modified,
            thumbnail = input.thumbnail?.toResponse(),
            resourceURI = input.resourceURI,
            comics = input.comics?.toResponse(),
            series = input.series?.toResponse(),
            stories = input.stories?.toResponse(),
            events = input.events?.toResponse(),
            urls = input.urls?.map { it.toResponse() }
        )

}

internal class ApiThumbnailMapper : Mapper<ApiThumbnail, Thumbnail> {
    override fun mapper(input: ApiThumbnail): Thumbnail = Thumbnail(
        path = input.path,
        extension = input.extension
    )

}

internal class ThumbnailMapper : Mapper<Thumbnail, ApiThumbnail> {
    override fun mapper(input: Thumbnail): ApiThumbnail = ApiThumbnail(
        path = input.path,
        extension = input.extension
    )

}

internal class ApiUrlMapper : Mapper<ApiURL, URL> {
    override fun mapper(input: ApiURL): URL = URL(
        type = input.type,
        url = input.url
    )
}

internal class UrlMapper : Mapper<URL, ApiURL> {
    override fun mapper(input: URL): ApiURL = ApiURL(
        type = input.type,
        url = input.url
    )
}

internal class ApiComicsMapper : Mapper<ApiComics, Comics> {
    override fun mapper(input: ApiComics): Comics = Comics(
        available = input.available,
        collectionURI = input.collectionURI,
        items = input.items?.map { it.toDomain() },
        returned = input.returned
    )
}

internal class ComicsMapper : Mapper<Comics, ApiComics> {
    override fun mapper(input: Comics): ApiComics = ApiComics(
        available = input.available,
        collectionURI = input.collectionURI,
        items = input.items?.map { it.toResponse() },
        returned = input.returned
    )
}

internal class ComicsItemMapper : Mapper<ComicsItem, ApiComicsItem> {
    override fun mapper(input: ComicsItem): ApiComicsItem = ApiComicsItem(
        resourceURI = input.resourceURI,
        name = input.name
    )

}

internal class ApiComicsItemMapper : Mapper<ApiComicsItem, ComicsItem> {
    override fun mapper(input: ApiComicsItem): ComicsItem = ComicsItem(
        resourceURI = input.resourceURI,
        name = input.name
    )

}


internal class StoriesMapper : Mapper<Stories, ApiStories> {
    override fun mapper(input: Stories): ApiStories = ApiStories(
        available = input.available,
        collectionURI = input.collectionURI,
        items = input.items?.map { it.toResponse() },
        returned = input.returned
    )

}

internal class ApiStoriesMapper : Mapper<ApiStories, Stories> {
    override fun mapper(input: ApiStories): Stories = Stories(
        available = input.available,
        collectionURI = input.collectionURI,
        items = input.items?.map { it.toDomain() },
        returned = input.returned
    )

}


internal class StoriesItemMapper : Mapper<StoriesItem, ApiStoriesItem> {
    override fun mapper(input: StoriesItem): ApiStoriesItem = ApiStoriesItem(
        resourceURI = input.resourceURI,
        name = input.name,
        type = input.type,
    )

}

internal class ApiStoriesItemMapper : Mapper<ApiStoriesItem, StoriesItem> {
    override fun mapper(input: ApiStoriesItem): StoriesItem = StoriesItem(
        resourceURI = input.resourceURI,
        name = input.name,
        type = input.type,
    )

}


internal fun ApiCharacter.toDomain() =
    ApiResourceMapper().mapper(this)

internal fun Character.toResponse() =
    ResourceMapper().mapper(this)

internal fun ApiThumbnail.toDomain() =
    ApiThumbnailMapper().mapper(this)

internal fun Thumbnail.toResponse() =
    ThumbnailMapper().mapper(this)


internal fun ApiURL.toDomain() =
    ApiUrlMapper().mapper(this)

internal fun URL.toResponse() =
    UrlMapper().mapper(this)

internal fun ApiComics.toDomain() =
    ApiComicsMapper().mapper(this)

internal fun Comics.toResponse() =
    ComicsMapper().mapper(this)

internal fun ApiComicsItem.toDomain() =
    ApiComicsItemMapper().mapper(this)

internal fun ComicsItem.toResponse() =
    ComicsItemMapper().mapper(this)

internal fun ApiStories.toDomain() =
    ApiStoriesMapper().mapper(this)

internal fun Stories.toResponse() =
    StoriesMapper().mapper(this)

internal fun ApiStoriesItem.toDomain() =
    ApiStoriesItemMapper().mapper(this)

internal fun StoriesItem.toResponse() =
    StoriesItemMapper().mapper(this)