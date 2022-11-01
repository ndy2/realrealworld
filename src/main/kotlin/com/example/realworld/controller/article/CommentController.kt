package com.example.realworld.controller.article

import com.example.realworld.domain.article.model.inout.AddComment
import com.example.realworld.domain.article.model.inout.CommentResponse
import com.example.realworld.domain.article.service.CommentService
import com.example.realworld.util.profileId
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/articles/{slug}/comments")
@RestController
class CommentController(
    private val service: CommentService
) {

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    fun add(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable slug: String,
        @RequestBody addComment: AddComment
    ): Any {
        val profileId = profileId(jwt)!!
        return view(service.add(profileId, slug, addComment))
    }

    @GetMapping
    fun getBySlug(
        @AuthenticationPrincipal jwt: Jwt?,
        @PathVariable slug: String
    ): Any {
        val profileId = profileId(jwt)
        return viewList(service.getByArticleSlug(profileId, slug))
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{commentId}")
    fun delete(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable slug: String,
        @PathVariable commentId: Long,
    ): ResponseEntity<*> {
        val profileId = profileId(jwt)!!
        service.delete(profileId, slug, commentId)
        return ResponseEntity.noContent().build<Any>()
    }

    fun view(commentResponse: CommentResponse) = mapOf("comment" to commentResponse)

    fun viewList(commentResponse: List<CommentResponse>) = mapOf(
        "comments" to commentResponse,
        "commentsCount" to commentResponse.size
    )
}