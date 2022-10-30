package com.example.realworld.util

import org.springframework.security.oauth2.jwt.Jwt

fun userId(jwt: Jwt) = jwt.getClaimAsString("userId").toLong()
