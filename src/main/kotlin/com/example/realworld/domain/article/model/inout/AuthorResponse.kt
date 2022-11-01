package com.example.realworld.domain.article.model.inout

data class AuthorResponse(
    val username: String,
    val bio: String?,
    val image: String?,
    val following: Boolean
)