package com.example.realworld.domain.tag.repository

import com.example.realworld.domain.tag.model.Tag
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository : JpaRepository<Tag, Long>{

    fun findByName(name: String): Tag?
}