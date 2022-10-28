package com.example.realworld.controller.user

import com.example.realworld.domain.user.model.inout.UpdateUser
import com.example.realworld.domain.user.service.UserService
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/user")
@RestController
class UserController(
    private val service: UserService
) {

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    fun currentUser(
        @AuthenticationPrincipal userId: Long,
        @RequestHeader(AUTHORIZATION) authHeader: String
    ): Any {
        return view(service.getById(userId).withToken(token(authHeader)))
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping
    fun update(
        @AuthenticationPrincipal userId: Long,
        @RequestHeader(AUTHORIZATION) authHeader: String,
        @RequestBody updateUser: UpdateUser
    ): Any {
        return view(service.update(userId, updateUser).withToken(token(authHeader)))
    }

    fun view(user: Any) = mapOf("user" to user)
    fun token(authHeader: String) = authHeader.substringAfter("Bearer ")
}