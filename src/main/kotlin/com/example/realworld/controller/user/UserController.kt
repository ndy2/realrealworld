package com.example.realworld.controller.user

import com.example.realworld.domain.user.model.inout.UpdateUser
import com.example.realworld.domain.user.service.UserService
import com.example.realworld.util.userId
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*

@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/user")
@RestController
class UserController(
    private val service: UserService
) {
    @GetMapping
    fun currentUser(
        @AuthenticationPrincipal jwt: Jwt
    ): Any {
        val userId = userId(jwt)
        return view(service.getById(userId).withToken(jwt.tokenValue))
    }

    @PutMapping
    fun update(
        @AuthenticationPrincipal jwt: Jwt,
        @RequestBody updateUser: UpdateUser
    ): Any {
        val userId = userId(jwt)
        return view(service.update(userId, updateUser).withToken(jwt.tokenValue))
    }

    fun view(user: Any) = mapOf("user" to user)
}