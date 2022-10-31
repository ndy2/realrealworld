package com.example.realworld.domain.tag.service

import com.example.realworld.domain.tag.model.Tag
import com.example.realworld.domain.tag.repository.TagRepository
import org.springframework.stereotype.Service

@Service
class TagService(
    private val repository: TagRepository
) {

    fun getAllTagNames(): List<String> {
        return repository.findAll().map { it.name }
    }

    fun getOrSave(name: String): Tag {
        return repository.findByName(name) ?: repository.save(Tag(name))
    }
}