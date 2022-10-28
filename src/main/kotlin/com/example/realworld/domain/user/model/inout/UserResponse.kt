package com.example.realworld.domain.user.model.inout

data class UserResponse(

    val email: String,
    val username: String,
    val bio: String?,
    val image: String?,
    val token: String,
)