package com.example.realworld.controller.profile

import com.example.realworld.domain.profile.service.ProfileService
import com.example.realworld.util.profileId
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestMethod.DELETE
import org.springframework.web.bind.annotation.RequestMethod.POST

@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/profiles")
@RestController
class ProfileController(
    private val service: ProfileService
) {

    @GetMapping("/{username}")
    fun profile(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable username: String,
    ): Any {
        val profileId = profileId(jwt)!!
        return view(service.getByUsername(profileId, username))
    }

    @RequestMapping("/{username}/follow", method = [POST, DELETE])
    fun followOrUnfollow(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable username: String,
    ): Any {
        val profileId = profileId(jwt)!!
        return view(service.followOrUnfollow(profileId, username))
    }


    private fun view(profileResponse: Any): Any = mapOf("profile" to profileResponse)
}