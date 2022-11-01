package com.example.realworld.domain.article.service

import com.example.realworld.domain.article.model.Article
import com.example.realworld.domain.article.model.inout.ArticleResponse
import com.example.realworld.domain.article.model.inout.AuthorResponse
import com.example.realworld.domain.article.model.inout.CreateArticle
import com.example.realworld.domain.article.model.inout.UpdateArticle
import com.example.realworld.domain.article.repository.ArticleRepository
import com.example.realworld.domain.profile.model.inout.ArticleSearchCond
import com.example.realworld.domain.profile.repository.ProfileRepository
import com.example.realworld.domain.tag.service.TagService
import com.example.realworld.exception.NotFoundException
import org.springframework.data.domain.Pageable
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
        val tags = (createArticle.tagList ?: emptyList())
            .map { tagService.getOrSave(it) }
            .toMutableList()
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

    fun getBySlug(profileId: Long?, slug: String): ArticleResponse {
        val currentUserProfile = profileId?.let { findProfileWithFollowingAndFavorite(it) }

        repository.findBySlugWithAuthorAndTag(slug)?.let {
            return ArticleResponse(
                it.slug,
                it.title,
                it.description,
                it.body,
                it.tags.map { it.name },
                it.createdAt,
                it.updatedAt,
                currentUserProfile?.isFavorite(it) ?: false,
                it.favoriteCount,
                AuthorResponse(
                    it.authorUsername,
                    it.authorBio,
                    it.authorImage,
                    currentUserProfile?.isFollowing(it.author) ?: false
                )
            )
        }
        throw NotFoundException("no such article slug : $slug")
    }

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

    @Transactional
    fun update(profileId: Long, slug: String, updateArticle: UpdateArticle): ArticleResponse {
        val (title, description, body) = updateArticle

        val currentUserProfile = findProfileWithFollowingAndFavorite(profileId)
        repository.findBySlugWithAuthorAndTag(slug)?.let {
            if (!it.isWrittenBy(currentUserProfile)) {
                throw NotFoundException("no such article slug : $slug")
            }

            it.update(title, description, body)

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
                    it.body,
                    it.authorImage,
                    currentUserProfile.isFollowing(it.author)
                )
            )

        }
        throw NotFoundException("no such article slug : $slug")
    }

    @Transactional
    fun delete(profileId: Long, slug: String) {
        val currentUserProfile = findProfileWithFollowingAndFavorite(profileId)
        repository.findBySlugWithAuthorAndTag(slug)?.let {
            if (!it.isWrittenBy(currentUserProfile)) {
                throw NotFoundException("no such article slug : $slug")
            }

            repository.delete(it)
        }
        throw NotFoundException("no such article slug : $slug")
    }

    fun getList(profileId: Long?, searchCond: ArticleSearchCond, pageable: Pageable): List<ArticleResponse> {
        val currentUserProfile = profileId?.let { findProfileWithFollowingAndFavorite(it) }
        return repository.findBySearchCond(searchCond, pageable).map {
            ArticleResponse(
                it.slug,
                it.title,
                it.description,
                it.body,
                it.tags.map { it.name },
                it.createdAt,
                it.updatedAt,
                currentUserProfile?.isFavorite(it) ?: false,
                it.favoriteCount,
                AuthorResponse(
                    it.authorUsername,
                    it.authorBio,
                    it.authorImage,
                    currentUserProfile?.isFollowing(it.author) ?: false
                )
            )
        }
    }

    fun getFeedList(profileId: Long, searchCond: ArticleSearchCond, pageable: Pageable): List<ArticleResponse> {
        val currentUserProfile = findProfileWithFollowingAndFavorite(profileId)
        return repository.findFeedBySearchCond(
            currentUserProfile.following.map { it.id },
            searchCond,
            pageable
        ).map {
            ArticleResponse(
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
                    true
                )
            )
        }
    }

    private fun findProfile(profileId: Long) =
        profileRepository.findByProfileId(profileId) ?: throw NotFoundException("no such profile id : $profileId")

    private fun findProfileWithFollowingAndFavorite(profileId: Long) =
        profileRepository.findByIdWithFollowingAndFavorite(profileId)
            ?: throw NotFoundException("no such profile id : $profileId")
}