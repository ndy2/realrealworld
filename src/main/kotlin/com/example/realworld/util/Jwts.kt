package com.example.realworld.util

import org.springframework.security.oauth2.jwt.Jwt

fun userId(jwt: Jwt) = jwt.getClaim("userId") as Long
