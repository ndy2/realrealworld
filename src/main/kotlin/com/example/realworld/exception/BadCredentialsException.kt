package com.example.realworld.exception

class BadCredentialsException(
    message: String? = null,
    e: Exception? = null
) : RuntimeException(message, e)