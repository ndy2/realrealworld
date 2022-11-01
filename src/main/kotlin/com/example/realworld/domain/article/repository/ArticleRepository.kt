package com.example.realworld.domain.article.repository

import com.example.realworld.domain.article.model.Article
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ArticleRepository : JpaRepository<Article, Long>, CustomArticleRepository {

    //@formatter:off
    @Query(
        "select a " +
        "from Article a " +
        "join fetch a.author au " +
        "join fetch a.tags t " +
        "where a.slug = :slug "
    )
    //@formatter:on
    fun findBySlugWithAuthorAndTag(slug: String): Article?

    fun findBySlug(slug: String): Article?
}