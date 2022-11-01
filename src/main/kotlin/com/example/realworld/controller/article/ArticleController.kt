package com.example.realworld.controller.article

import com.example.realworld.domain.article.model.inout.CreateArticle
import com.example.realworld.domain.article.service.ArticleService
import com.example.realworld.domain.profile.model.inout.ArticleSearchCond
import com.example.realworld.util.profileId
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.DELETE
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/articles")
@RestController
class ArticleController(
    private val service: ArticleService
) {

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    fun create(
        @AuthenticationPrincipal jwt: Jwt,
        @RequestBody createArticle: CreateArticle
    ): Any {
        val profileId = profileId(jwt)!!
        return view(service.create(profileId, createArticle))
    }

    @GetMapping("/{slug}")
    fun get(
        @AuthenticationPrincipal jwt: Jwt?,
        @PathVariable slug: String
    ): Any {
        val profileId = profileId(jwt)
        return view(service.getBySlug(profileId, slug))
    }

    @GetMapping
    fun list(
        @AuthenticationPrincipal jwt: Jwt?,
        searchCond: ArticleSearchCond,
        @PageableDefault pageable: Pageable
    ): Any {
        val profileId = profileId(jwt)
        return view(service.getList(profileId, searchCond, pageable))
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/{slug}/favorite", method = [POST, DELETE])
    fun favoriteOrUnfavorite(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable slug: String
    ): Any {
        val profileId = profileId(jwt)!!
        return view(service.favoriteOrUnfavorite(profileId, slug))
    }

    private fun view(articleResponse: Any): Any = mapOf("article" to articleResponse)
}