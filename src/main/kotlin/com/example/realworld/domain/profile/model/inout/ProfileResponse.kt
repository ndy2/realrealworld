package com.example.realworld.domain.profile.model.inout

data class ProfileResponse(

    val username: String,
    val bio: String?,
    val image: String?,

    /* 현재 사용자의 조회 대상 프로필 사용자 팔로우 여부 */
    val following: Boolean
)