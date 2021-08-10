package com.github.jmlb23.marvel


sealed class Errors {
    data class ApiError(val trace: String): Errors()
    object NotSupported : Errors()
}

data class Character (
    val id: Long? = null,
    val name: String? = null,
    val description: String? = null,
    val modified: String? = null,
    val thumbnail: Thumbnail? = null,
    val resourceURI: String? = null,
    val comics: Comics? = null,
    val series: Comics? = null,
    val stories: Stories? = null,
    val events: Comics? = null,
    val urls: List<URL>? = null
)

data class Comics (
    val available: Long? = null,
    val collectionURI: String? = null,
    val items: List<ComicsItem>? = null,
    val returned: Long? = null
)

data class ComicsItem (
    val resourceURI: String? = null,
    val name: String? = null
)

data class Stories (
    val available: Long? = null,
    val collectionURI: String? = null,
    val items: List<StoriesItem>? = null,
    val returned: Long? = null
)

data class StoriesItem (
    val resourceURI: String? = null,
    val name: String? = null,
    val type: String? = null
)


data class Thumbnail (
    val path: String? = null,
    val extension: String? = null
)

data class URL (
    val type: String? = null,
    val url: String? = null
)

