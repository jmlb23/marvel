package com.github.jmlb23.marvel

internal fun Int.fromPage(): Pair<Int, Int> =
    Pair(100, this * 100)
