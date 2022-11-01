package com.example.realworld.domain.profile.model

import com.example.realworld.domain.article.model.Article
import javax.persistence.*

@Table(name = "profile")
@Entity
class Profile(
    @Column(name = "email", nullable = false)
    var username: String,

    @Column(name = "bio", nullable = true)
    var bio: String? = null,

    @Column(name = "image", nullable = true)
    var image: String? = null,
) {
    @Id
    @GeneratedValue
    var id: Long = 0L

    @ManyToMany
    @JoinTable(
        name = "follow",
        joinColumns = [JoinColumn(name = "follower_id")],
        inverseJoinColumns = [JoinColumn(name = "followee_id")]
    )
    var following: MutableSet<Profile> = mutableSetOf()

    @ManyToMany
    @JoinTable(
        name = "favorite",
        joinColumns = [JoinColumn(name = "profile_id")],
        inverseJoinColumns = [JoinColumn(name = "article_id")]
    )
    var favorites: MutableSet<Article> = mutableSetOf()

    fun update(username: String?, bio: String?, image: String?) {
        username?.let { this.username = username }
        bio?.let { this.bio = bio }
        image?.let { this.image = image }
    }

    fun isFollowing(profile: Profile): Boolean {
        return following.contains(profile)
    }

    fun followOrUnfollow(profile: Profile) {
        require(this.id != profile.id) { "you can follow/unfollow your self" }
        if (isFollowing(profile)) {
            following.remove(profile)
        } else {
            following.add(profile)
        }
    }

    fun isFavorite(article: Article): Boolean {
        return favorites.contains(article)
    }

    fun favoriteOrUnfavorite(article: Article) {
        if (isFavorite(article)) {
            favorites.remove(article)
            article.subFavoriteCount()
        } else {
            favorites.add(article)
            article.addFavoriteCount()
        }
    }
}