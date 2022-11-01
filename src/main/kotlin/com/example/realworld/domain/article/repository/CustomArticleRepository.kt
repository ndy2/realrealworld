package com.example.realworld.domain.article.repository

import com.example.realworld.domain.article.model.Article
import com.example.realworld.domain.profile.model.inout.ArticleSearchCond
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

interface CustomArticleRepository {

    fun findBySearchCond(
        searchCond: ArticleSearchCond = ArticleSearchCond(),
        pageable: Pageable = PageRequest.of(0, 20, Sort.Direction.DESC, "id")
    ): List<Article>

    fun findFeedBySearchCond(
        followingIds: List<Long>,
        searchCond: ArticleSearchCond = ArticleSearchCond(),
        pageable: Pageable = PageRequest.of(0, 20, Sort.Direction.DESC, "id")
    ): List<Article>
}