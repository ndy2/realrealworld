package com.example.realworld.domain.article.model.inout

import java.time.Instant
import java.util.*

data class ArticleResponse(
    val slug: String,
    val title: String,
    val description: String,
    val body: String,
    val tagList: List<String>,
    val createdAt: Instant,
    val updatedAt: Instant,
    val favorited: Boolean,
    val favoritesCount: Long,
    val author: AuthorResponse,
) {

    // move given tag to 0th index
    fun reorderTags(tag: String) {
        Collections.swap(tagList, 0, tagList.indexOf(tag))
    }
}

