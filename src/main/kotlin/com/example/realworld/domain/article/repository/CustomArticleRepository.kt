package com.example.realworld.domain.article.repository

import com.example.realworld.domain.article.model.Article
import com.example.realworld.domain.profile.model.inout.ArticleSearchCond
import org.springframework.data.domain.Pageable

interface CustomArticleRepository {

    fun findBySearchCond(searchCond: ArticleSearchCond, pageable: Pageable): List<Article>
}