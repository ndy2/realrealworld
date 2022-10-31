package com.example.realworld.domain.profile.model.inout

data class ProfileResponse(

    val username: String,
    val bio: String?,
    val image: String?,
    val following: Boolean
)