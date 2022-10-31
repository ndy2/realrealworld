package com.example.realworld.controller.tag

import com.example.realworld.domain.tag.service.TagService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/tags")
@RestController
class TagController(
    private val service: TagService
) {

    @GetMapping
    fun tags(): Any {
        return view(service.getAllTagNames())
    }

    private fun view(tagResponse: Any) = mapOf("tags" to tagResponse)
}