package com.example.realworld.domain.article.model

import com.example.realworld.domain.profile.model.Profile
import java.time.Instant
import javax.persistence.*
import javax.persistence.FetchType.LAZY

@Entity
class Comment(
    @Column(name = "title", nullable = false)
    var body: String,

    @ManyToOne(fetch = LAZY, optional = false)
    var article: Article,

    @ManyToOne(fetch = LAZY, optional = false)
    var author: Profile
) {

    @Id
    @GeneratedValue
    var id: Long = 0L

    @Column(name = "createdAt", nullable = false)
    var createdAt: Instant = Instant.now()

    @Column(name = "updatedAt", nullable = false)
    var updatedAt: Instant = Instant.now()

    val authorUsername: String
        get() = author.username

    val authorBio: String?
        get() = author.bio
    val authorImage: String?
        get() = author.image

    fun isWrittenBy(profile: Profile) = this.author.id == profile.id
}