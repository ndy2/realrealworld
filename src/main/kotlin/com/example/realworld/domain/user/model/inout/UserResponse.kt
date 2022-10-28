package com.example.realworld.domain.user.model.inout

data class UserResponse(

    val email: String,
    val username: String,
    val bio: String?,
    val image: String?,
    var token: String = ""
) {
    fun withToken(token: String): UserResponse {
        this.token = token
        return this
    }
}