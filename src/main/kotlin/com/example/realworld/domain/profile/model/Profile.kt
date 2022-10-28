package com.example.realworld.domain.profile.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "profile")
@Entity
class Profile(
    var username: String,
    var bio: String? = null,
    var image: String? = null,
) {

    @Id
    @GeneratedValue
    var id: Long = 0L
}