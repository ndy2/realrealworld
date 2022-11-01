package com.example.realworld.domain.article.service

import com.example.realworld.domain.article.model.Comment
import com.example.realworld.domain.article.model.inout.AddComment
import com.example.realworld.domain.article.model.inout.AuthorResponse
import com.example.realworld.domain.article.model.inout.CommentResponse
import com.example.realworld.domain.article.repository.ArticleRepository
import com.example.realworld.domain.article.repository.CommentRepository
import com.example.realworld.domain.profile.repository.ProfileRepository
import com.example.realworld.exception.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val repository: CommentRepository,
    private val profileRepository: ProfileRepository,
    private val articleRepository: ArticleRepository
) {

    @Transactional
    fun add(profileId: Long, slug: String, addComment: AddComment): CommentResponse {
        val (body) = addComment
        val article = articleRepository.findBySlug(slug) ?: throw NotFoundException("no such article slug : $slug")
        val author = profileRepository.findByProfileId(profileId)!!

        val comment = repository.save(Comment(body, article, author))

        return CommentResponse(
            comment.id,
            comment.createdAt,
            comment.updatedAt,
            comment.body,
            AuthorResponse(
                comment.authorUsername,
                comment.authorBio,
                comment.authorImage,
                false   // it is your own comment following is always false
            )
        )
    }


    @Transactional(readOnly = true)
    fun getByArticleSlug(profileId: Long?, slug: String): List<CommentResponse> {
        val currentUserProfile = profileId?.let { getCurrentUserProfile(profileId) }

        return repository.findByArticleSlugWithAuthor(slug).map {
            CommentResponse(
                it.id,
                it.createdAt,
                it.updatedAt,
                it.body,
                AuthorResponse(
                    it.authorUsername,
                    it.authorBio,
                    it.authorImage,
                    currentUserProfile?.isFollowing(it.author) ?: false
                )
            )
        }
    }

    @Transactional
    fun delete(profileId: Long, slug: String, commentId: Long) {
        val currentUserProfile = getCurrentUserProfile(profileId)
        val comment = repository.findByArticleSlugAndIdWithAuthor(slug, commentId)
            ?: throw NotFoundException("no such comment id : $commentId")

        if (!comment.isWrittenBy(currentUserProfile)) {
            throw NotFoundException("no such comment id : $commentId")
        }

        repository.delete(comment)
    }

    private fun getCurrentUserProfile(profileId: Long) =
        profileRepository.findByIdWithFollowing(profileId) ?: throw NotFoundException("no such profile id : $profileId")

}