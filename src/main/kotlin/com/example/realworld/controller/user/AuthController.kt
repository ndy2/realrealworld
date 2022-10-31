package com.example.realworld.controller.user

import com.example.realworld.domain.user.model.inout.Login
import com.example.realworld.domain.user.model.inout.Register
import com.example.realworld.domain.user.service.AuthService
import com.example.realworld.exception.BadCredentialsException
import com.example.realworld.exception.NotFoundException
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/users")
@RestController
class AuthController(
    private val service: AuthService
) {

    @PostMapping("/login")
    fun login(
        @RequestBody login: Login,
        errors: Errors
    ): Any {
        try {
            return view(service.login(login))
        } catch (ex: NotFoundException) {
            throw BadCredentialsException("login failure!", ex)
        }
    }

    @PostMapping
    fun register(
        @RequestBody register: Register
    ): Any {
        return view(service.register(register))
    }


    fun view(userResponse: Any): Any = mapOf("user" to userResponse)
}