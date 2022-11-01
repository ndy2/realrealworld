package com.example.realworld.domain.article.model.inout

import com.example.realworld.exception.SelfValidating
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class AddComment(

    @field:NotNull(message = "can't be missing")
    @field:NotEmpty(message = "can't be empty")
    val body: String
) : SelfValidating<AddComment>() {

    init {
        validateSelf()
    }
}