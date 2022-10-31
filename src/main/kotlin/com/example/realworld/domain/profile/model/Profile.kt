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
    var following: Set<Profile> = HashSet()

    fun update(username: String?, bio: String?, image: String?) {
        if (username != null) this.username = username
        if (bio != null) this.bio = bio
        if (image != null) this.image = image
    }

    fun followingProfileId(profileId: Long): Boolean {
        return following.map { it.id }.contains(profileId)
    }
}