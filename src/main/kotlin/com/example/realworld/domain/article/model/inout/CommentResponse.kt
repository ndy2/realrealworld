package com.example.realworld.domain.article.model.inout

import java.time.Instant

data class CommentResponse(
    val id: Long,
    val createdAt: Instant,
    val updatedAt: Instant,
    val body: String,
    val author: AuthorResponse
)

