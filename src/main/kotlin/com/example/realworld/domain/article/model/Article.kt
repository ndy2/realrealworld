package com.example.realworld.domain.article.model

import com.example.realworld.domain.profile.model.Profile
import com.example.realworld.domain.tag.model.Tag
import org.hibernate.annotations.BatchSize
import java.time.Instant
import java.time.Instant.now
import javax.persistence.*
import javax.persistence.FetchType.LAZY

@Entity
class Article(
    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "description", nullable = false)
    var description: String,

    @Column(name = "body", nullable = false)
    var body: String,

    @ManyToMany
    @BatchSize(size = 20)
    var tags: MutableList<Tag> = mutableListOf(),

    @ManyToOne(fetch = LAZY)
    var author: Profile
) {

    @Id
    @GeneratedValue
    var id: Long = 0L

    @Column(name = "slug", nullable = false)
    var slug: String = updateSlug()

    @Column(name = "createdAt", nullable = false)
    var createdAt: Instant = now()

    @Column(name = "updatedAt", nullable = false)
    var updatedAt: Instant = now()

    @Column(name = "favorite_count", nullable = false)
    var favoriteCount: Long = 0L

    @Version
    val version: Long = 0L

    val authorUsername: String
        get() = author.username

    val authorBio: String?
        get() = author.bio
    val authorImage: String?
        get() = author.image

    fun update(title: String?, description: String?, body: String?) {
        require(title != null || description != null || body != null)
        { "require at least one non-null property to update" }

        title?.let {
            this.title = title
            this.slug = updateSlug()
        }
        description?.let { this.description = description }
        body?.let { this.body = body }

        this.updatedAt = now()
    }

    fun addFavoriteCount() {
        favoriteCount += 1
    }

    fun subFavoriteCount() {
        favoriteCount -= 1
    }

    fun isWrittenBy(profile: Profile): Boolean {
        return this.author.id == profile.id
    }

    private fun updateSlug() = title.replace(" ", "-")
}