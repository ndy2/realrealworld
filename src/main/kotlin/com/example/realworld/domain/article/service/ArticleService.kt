package com.example.realworld.domain.article.service

import com.example.realworld.domain.article.model.Article
import com.example.realworld.domain.article.model.inout.ArticleResponse
import com.example.realworld.domain.article.model.inout.AuthorResponse
import com.example.realworld.domain.article.model.inout.CreateArticle
import com.example.realworld.domain.article.repository.ArticleRepository
import com.example.realworld.domain.profile.repository.ProfileRepository
import com.example.realworld.domain.tag.service.TagService
import com.example.realworld.exception.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ArticleService(
    private val repository: ArticleRepository,
    private val profileRepository: ProfileRepository,
    private val tagService: TagService
) {

    @Transactional
    fun create(profileId: Long, createArticle: CreateArticle): ArticleResponse {
        val (title, description, body, _) = createArticle
        val tags = (createArticle.tagList ?: emptyList()).map { tagService.getOrSave(it) }
        val author = findProfile(profileId)

        val article = Article(title, description, body, tags, author)
        repository.save(article)

        return ArticleResponse(
            article.slug,
            article.title,
            article.description,
            article.body,
            article.tags.map { it.name },
            article.createdAt,
            article.updatedAt,
            false,
            0L,
            AuthorResponse(
                author.username,
                author.bio,
                author.image,
                false
            )
        )
    }

    fun getBySlug(profileId: Long, slug: String): ArticleResponse {
        val currentUserProfile = findProfileWithFollowingAndFavorite(profileId)
        repository.findBySlugWithAuthorAndTag(slug)?.let {
            return ArticleResponse(
                it.slug,
                it.title,
                it.description,
                it.body,
                it.tags.map { it.name },
                it.createdAt,
                it.updatedAt,
                currentUserProfile.isFavorite(it),
                it.favoriteCount,
                AuthorResponse(
                    it.authorUsername,
                    it.authorBio,
                    it.authorImage,
                    currentUserProfile.isFollowing(it.author)
                )
            )
        }
        throw NotFoundException("no such article slug : $slug")
    }

    private fun findProfile(profileId: Long) =
        profileRepository.findByProfileId(profileId) ?: throw NotFoundException("no such profile id : $profileId")

    private fun findProfileWithFollowingAndFavorite(profileId: Long) =
        profileRepository.findByIdWithFollowingAndFavorite(profileId)
            ?: throw NotFoundException("no such profile id : $profileId")

    @Transactional
    fun favoriteOrUnfavorite(profileId: Long, slug: String): ArticleResponse {
        val currentUserProfile = findProfileWithFollowingAndFavorite(profileId)
        repository.findBySlugWithAuthorAndTag(slug)?.let {
            currentUserProfile.favoriteOrUnfavorite(it)

            return ArticleResponse(
                it.slug,
                it.title,
                it.description,
                it.body,
                it.tags.map { it.name },
                it.createdAt,
                it.updatedAt,
                currentUserProfile.isFavorite(it),
                it.favoriteCount,
                AuthorResponse(
                    it.authorUsername,
                    it.authorBio,
                    it.authorImage,
                    currentUserProfile.isFollowing(it.author)
                )
            )
        }
        throw NotFoundException("no such article slug : $slug")
    }

}