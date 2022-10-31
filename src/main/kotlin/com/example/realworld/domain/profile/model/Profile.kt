package com.example.realworld.domain.profile.model

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

    fun update(username: String?, bio: String?, image: String?) {
        if (username != null) this.username = username
        if (bio != null) this.bio = bio
        if (image != null) this.image = image
    }

    fun isFollowing(profile: Profile): Boolean {
        return following.contains(profile)
    }

    fun followOrUnfollow(profile: Profile) {
        if (isFollowing(profile)) {
            following.remove(profile)
        } else {
            following.add(profile)
        }
    }
}