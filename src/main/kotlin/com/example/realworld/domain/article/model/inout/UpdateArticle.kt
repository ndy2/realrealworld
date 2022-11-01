package com.example.realworld.domain.article.model.inout

import com.example.realworld.exception.SelfValidating
import com.fasterxml.jackson.annotation.JsonRootName
import javax.validation.constraints.NotEmpty

@JsonRootName("article")
data class UpdateArticle(

    @field:NotEmpty(message = "can't be empty")
    val title: String?,

    @field:NotEmpty(message = "can't be empty")
    val description: String?,

    @field:NotEmpty(message = "can't be empty")
    val body: String?,

    ) : SelfValidating<UpdateArticle>() {
    init {
        validateSelf()
    }
}