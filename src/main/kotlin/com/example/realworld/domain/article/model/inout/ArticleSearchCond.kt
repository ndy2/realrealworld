package com.example.realworld.domain.article.model.inout

data class ArticleSearchCond(

    /* filter by tag */
    val tag: String? = null,

    /* filter by author username */
    val author: String? = null,

    /* filter by favorited by user with username */
    val favorited: String? = null
)