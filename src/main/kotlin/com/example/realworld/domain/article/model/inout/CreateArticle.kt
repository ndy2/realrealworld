package com.example.realworld.domain.article.model.inout

import com.example.realworld.exception.SelfValidating
import com.fasterxml.jackson.annotation.JsonRootName
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@JsonRootName("article")
data class CreateArticle(

    @field:NotNull(message = "can't be missing")
    @field:NotEmpty(message = "can't be empty")
    val title: String,

    @field:NotNull(message = "can't be missing")
    @field:NotEmpty(message = "can't be empty")
    val description: String,

    @field:NotNull(message = "can't be missing")
    @field:NotEmpty(message = "can't be empty")
    val body: String,

    val tagList: List<String>?
) : SelfValidating<CreateArticle>() {

    init {
        validateSelf()
    }
}