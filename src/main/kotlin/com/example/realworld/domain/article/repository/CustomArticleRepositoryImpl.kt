package com.example.realworld.domain.article.repository

import com.example.realworld.common.querydsl.Querydsl5RepositorySupport
import com.example.realworld.domain.article.model.Article
import com.example.realworld.domain.article.model.QArticle.article
import com.example.realworld.domain.profile.model.QProfile
import com.example.realworld.domain.profile.model.QProfile.*
import com.example.realworld.domain.profile.model.inout.ArticleSearchCond
import com.example.realworld.domain.tag.model.QTag.tag
import org.springframework.data.domain.Pageable

class CustomArticleRepositoryImpl : Querydsl5RepositorySupport(Article::class.java),
    CustomArticleRepository {

    override fun findBySearchCond(searchCond: ArticleSearchCond, pageable: Pageable): List<Article> {
        val (tag, author, favorited) = searchCond

        return applyPagination(
            pageable,
            selectFrom(article)
                .join(article.author).fetchJoin()
                .where(
                    nullSafeBuilder { article.id.`in`(articleIdContainingTagName(tag)) },
                    nullSafeBuilder { article.author.username.eq(author) },
                    nullSafeBuilder { article.id.`in`(articleInFavoritedByUser(favorited)) },
                )
        ) { it.tags.forEach { tag -> tag.name } }
    }

    override fun findFeedBySearchCond(
        followingIds: List<Long>,
        searchCond: ArticleSearchCond,
        pageable: Pageable
    ): List<Article> {
        val (tag, author, favorited) = searchCond
        return applyPagination(
            pageable,
            selectFrom(article)
                .join(article.author).fetchJoin()
                .where(
                    article.id.`in`(articleIdWrittenByFollowingUser(followingIds)),
                    nullSafeBuilder { article.id.`in`(articleIdContainingTagName(tag)) },
                    nullSafeBuilder { article.author.username.eq(author) },
                    nullSafeBuilder { article.id.`in`(articleInFavoritedByUser(favorited)) },
                )
        ) { it.tags.forEach { tag -> tag.name } }
    }

    /* filter by tag */
    private fun articleIdContainingTagName(name: String?): List<Long>? {
        name?.let {
            return select(article.id)
                .from(article)
                .join(article.tags, tag)
                .where(tag.name.eq(name))
                .fetch()
        }
        return null
    }

    /* filter by favorited by user with username */
    private fun articleInFavoritedByUser(username: String?): List<Long>? {

        username?.let {
            return select(article.id)
                .from(profile)
                .join(profile.favorites, article)
                .where(
                    profile.username.eq(username)
                )
                .fetch()
        }
        return null
    }

    /* filter feed request */
    private fun articleIdWrittenByFollowingUser(followingIds: List<Long>): List<Long> {
        return select(article.id)
            .from(article)
            .join(article.author)
            .where(
                article.author.id.`in`(followingIds)
            )
            .fetch()
    }
}