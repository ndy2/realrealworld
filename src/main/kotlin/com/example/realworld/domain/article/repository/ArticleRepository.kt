package com.example.realworld.domain.article.repository

import com.example.realworld.domain.article.model.Article
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleRepository : JpaRepository<Article, Long> {
}