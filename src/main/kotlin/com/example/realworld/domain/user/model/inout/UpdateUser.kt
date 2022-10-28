package com.example.realworld.domain.user.model.inout

import com.example.realworld.exception.SelfValidating
import com.fasterxml.jackson.annotation.JsonRootName
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@JsonRootName("user")
data class UpdateUser(

    @field:Size(min = 1, message = "can't be empty")
    @field:Pattern(
        regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$",
        message = "must be a valid email"
    )
    val email: String?,

    @field:Size(min = 1, message = "can't be empty")
    val password: String?,

    @field:Size(min = 1, message = "can't be empty")
    @field:Pattern(regexp = "^\\w+$", message = "must be alphanumeric")
    val username: String?,

    val image: String?,

    val bio: String?,

    ) : SelfValidating<Login>() {
    init {
        validateSelf()
    }
}