package com.example.realworld.domain.profile.repository

import com.example.realworld.domain.profile.model.inout.ArticleSearchCond
import org.springframework.data.domain.Pageable

class CustomProfileRepositoryImpl : CustomProfileRepository {


    override fun findBySearchCond(searchCond: ArticleSearchCond, pageable: Pageable) {


        TODO("Not yet implemented")
    }
}