package com.example.realworld.domain.user.model.inout

import com.example.realworld.exception.SelfValidating
import com.fasterxml.jackson.annotation.JsonRootName
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size


@JsonRootName("user")
class Register(

    @field:NotNull(message = "can't be missing")
    @field:Size(min = 1, message = "can't be empty")
    @field:Pattern(
        regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$",
        message = "must be a valid email"
    )
    private val email: String?,


    @NotNull(message = "can't be missing")
    @Size(min = 1, message = "can't be empty")
    private val password: String?,

    @NotNull(message = "can't be missing")
    @Size(min = 1, message = "can't be empty")
    @Pattern(regexp = "^\\w+$", message = "must be alphanumeric")
    private val username: String?,
) : SelfValidating<Register>() {
    init {
        validateSelf()
    }

    operator fun component1() = email!!
    operator fun component2() = password!!
    operator fun component3() = username!!
}