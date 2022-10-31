package com.example.realworld.domain.profile.repository

import com.example.realworld.domain.profile.model.inout.ArticleSearchCond
import org.springframework.data.domain.Pageable

interface CustomProfileRepository {

    fun findBySearchCond(searchCond: ArticleSearchCond, pageable: Pageable)
}