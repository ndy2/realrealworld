package com.example.realworld.domain.article.model.inout

import java.time.Instant

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
)

data class AuthorResponse(
    val username: String,
    val bio : String?,
    val image: String?,
    val following: Boolean
)
