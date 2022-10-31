package com.example.realworld.domain.tag.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Tag(
    @Column(name = "name", unique = true, nullable = false)
    var name: String
) {
    @Id
    @GeneratedValue
    var id: Long = 0L
}