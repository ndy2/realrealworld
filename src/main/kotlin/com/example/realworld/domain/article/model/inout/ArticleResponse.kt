package com.example.realworld.domain.tag.model.inout

import java.time.LocalDateTime

data class ArticleResponse(

    val slug: String,
    val title: String,
    val description: String,
    val body: String,
    val tagList: List<String>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val favorited: Boolean,
    val favoriteCount: Long,
    val author: AuthorResponse,
)

data class AuthorResponse(
    val username: String,
    val bio : String?,
    val image: String?,
    val following: Boolean
)
