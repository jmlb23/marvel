package com.github.jmlb23.marvel.data.remote

import kotlinx.serialization.Serializable

@Serializable
internal data class CharactersResponse (
    val code: Long? = null,
    val status: String? = null,
    val copyright: String? = null,
    val attributionText: String? = null,
    val attributionHTML: String? = null,
    val etag: String? = null,
    val data: Page? = null
)

@Serializable
internal data class Page (
    val offset: Long? = null,
    val limit: Long? = null,
    val total: Long? = null,
    val count: Long? = null,
    val results: List<ApiCharacter>? = null
)

@Serializable
internal data class ApiCharacter (
    val id: Long? = null,
    val name: String? = null,
    val description: String? = null,
    val modified: String? = null,
    val thumbnail: ApiThumbnail? = null,
    val resourceURI: String? = null,
    val comics: ApiComics? = null,
    val series: ApiComics? = null,
    val stories: ApiStories? = null,
    val events: ApiComics? = null,
    val urls: List<ApiURL>? = null
)

@Serializable
internal data class ApiComics (
    val available: Long? = null,
    val collectionURI: String? = null,
    val items: List<ApiComicsItem>? = null,
    val returned: Long? = null
)

@Serializable
internal data class ApiComicsItem (
    val resourceURI: String? = null,
    val name: String? = null
)

@Serializable
internal data class ApiStories (
    val available: Long? = null,
    val collectionURI: String? = null,
    val items: List<ApiStoriesItem>? = null,
    val returned: Long? = null
)

@Serializable
internal data class ApiStoriesItem (
    val resourceURI: String? = null,
    val name: String? = null,
    val type: String? = null
)

@Serializable
internal data class ApiThumbnail (
    val path: String? = null,
    val extension: String? = null
)


@Serializable
internal data class ApiURL (
    val type: String? = null,
    val url: String? = null
)

