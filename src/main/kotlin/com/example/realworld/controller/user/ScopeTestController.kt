package com.example.realworld.controller.user

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RequestMapping("/api/scope/photo")
@RestController
class ScopeTestController {

    @PreAuthorize("hasAuthority('SCOPE_photo')")
    @GetMapping
    fun photo() = "you have scope photo"
}