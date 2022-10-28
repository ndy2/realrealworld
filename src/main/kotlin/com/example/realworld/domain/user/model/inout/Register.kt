package com.example.realworld.domain.user.model.inout

import com.fasterxml.jackson.annotation.JsonRootName


@JsonRootName("user")
data class Register(
    val email: String = "",
    val password: String = "",
    val username: String = "",
)