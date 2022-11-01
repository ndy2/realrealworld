package com.example.realworld.controller.article

import com.example.realworld.domain.article.model.inout.ArticleResponse
import com.example.realworld.domain.article.model.inout.CreateArticle
import com.example.realworld.domain.article.model.inout.UpdateArticle
import com.example.realworld.domain.article.service.ArticleService
import com.example.realworld.domain.article.model.inout.ArticleSearchCond
import com.example.realworld.util.profileId
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestMethod.DELETE
import org.springframework.web.bind.annotation.RequestMethod.POST

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

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{slug}")
    fun update(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable slug: String,
        @RequestBody updateArticle: UpdateArticle
    ): Any {
        val profileId = profileId(jwt)!!
        return view(service.update(profileId, slug, updateArticle))
    }


    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{slug}")
    fun delete(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable slug: String
    ): ResponseEntity<*> {
        val profileId = profileId(jwt)!!
        service.delete(profileId, slug)
        return noContent().build<Any>()
    }

    @GetMapping
    fun list(
        @AuthenticationPrincipal jwt: Jwt?,
        searchCond: ArticleSearchCond,
        @PageableDefault pageable: Pageable
    ): Any {
        val profileId = profileId(jwt)
        val articleResponse = service.getList(profileId, searchCond, pageable)
        searchCond.tag?.let { articleResponse.forEach {it.reorderTags(searchCond.tag)} }

        return viewList(articleResponse)
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/feed")
    fun feed(
        @AuthenticationPrincipal jwt: Jwt,
        searchCond: ArticleSearchCond,
        @PageableDefault pageable: Pageable
    ): Any {
        val profileId = profileId(jwt)!!
        val articleResponse = service.getFeedList(profileId, searchCond, pageable)
        searchCond.tag?.let { articleResponse.forEach {it.reorderTags(searchCond.tag)} }

        return viewList(articleResponse)
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

    private fun view(articleResponse: ArticleResponse): Any = mapOf("article" to articleResponse)
    private fun viewList(articleResponse: List<ArticleResponse>): Any = mapOf(
        "articles" to articleResponse,
        "articlesCount" to articleResponse.size
    )
}