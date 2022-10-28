package com.example.realworld.security.token

import com.example.realworld.domain.user.model.User
import com.example.realworld.domain.user.service.TokenProvider
import org.springframework.stereotype.Component

@Component
class NullTokenProvider
    : TokenProvider{

    override fun getToken(user: User): String {
        return "null"
    }
}