package com.example.realworld.domain.article.model.inout

import com.example.realworld.exception.SelfValidating
import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName("article")
data class UpdateArticle(
    val title: String?,

    val description: String?,

    val body: String?,

    ) : SelfValidating<UpdateArticle>() {
    init {
        validateSelf()
    }
}