package com.example.realworld.domain.user.service

import com.example.realworld.domain.user.model.User


interface TokenProvider {

    fun getToken(user: User): String
}