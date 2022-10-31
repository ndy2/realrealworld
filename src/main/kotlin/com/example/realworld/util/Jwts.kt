package com.example.realworld.util

import org.springframework.security.oauth2.jwt.Jwt

fun userId(jwt: Jwt): Long = jwt.getClaim("userId")

fun profileId(jwt: Jwt?): Long? = jwt?.getClaim("profileId")
