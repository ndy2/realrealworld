package com.example.realworld.domain.profile.model.inout

data class ArticleSearchCond(

    /* filter by tag */
    val tag: String,

    /* filter by author username */
    val author: String,

    /* filter by favorited by user with username */
    val favorited: String
)