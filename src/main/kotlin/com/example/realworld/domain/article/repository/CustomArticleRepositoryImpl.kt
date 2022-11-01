package com.example.realworld.domain.article.repository

import com.example.realworld.common.querydsl.Querydsl5RepositorySupport
import com.example.realworld.domain.article.model.Article
import com.example.realworld.domain.article.model.QArticle.article
import com.example.realworld.domain.profile.model.QProfile
import com.example.realworld.domain.profile.model.QProfile.*
import com.example.realworld.domain.profile.model.inout.ArticleSearchCond
import com.example.realworld.domain.tag.model.QTag
import com.example.realworld.domain.tag.model.QTag.tag
import com.querydsl.jpa.JPAExpressions.selectFrom
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import java.util.function.Consumer

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

    /* filter by tag
    toMany join, paging 을 처리하기 위한 subQuery */
    private fun articleIdContainingTagName(name: String?): List<Long> {
        return select(article.id)
            .from(article)
            .join(article.tags, tag)
            .where(tag.name.eq(name))
            .fetch()
    }

    /* filter by favorited by user with username */
    private fun articleInFavoritedByUser(username: String?): List<Long> {
        return select(article.id)
            .from(article)
            .join(profile.favorites)
            .where(
                nullSafeBuilder { profile.username.eq(username) }
            )
            .fetch()
    }
}