package ru.spbstu.search.search.domain

data class SearchResult(val userId: Long, val name: String, val login: String, val pictureId: Long?, val isFav: Boolean)