package com.example.realworld.domain.article.repository

import com.example.realworld.domain.article.model.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CommentRepository : JpaRepository<Comment, Long> {

    //@formatter:off
    @Query(
        "select c " +
        "from Comment c " +
        "join c.article ar " +
        "join fetch c.author au " +
        "where ar.slug = :slug" +
        ""
    )
    //@formatter:on
    fun findByArticleSlugWithAuthor(slug: String): List<Comment>

    //@formatter:off
    @Query(
        "select c " +
        "from Comment c " +
        "join c.article ar " +
        "join fetch c.author au " +
        "where ar.slug = :slug " +
        "and c.id = :id"
    )
    //@formatter:on
    fun findByArticleSlugAndIdWithAuthor(slug: String, id: Long): Comment?
}