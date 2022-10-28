package com.example.realworld.domain.user.model

import com.example.realworld.domain.profile.model.Profile
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.CascadeType.ALL
import javax.persistence.Column
import javax.persistence.FetchType.LAZY


@Table(name = "users")
@Entity
class User(
    @Column(name = "email", unique = true, nullable = false)
    var email: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @OneToOne(cascade = [ALL], fetch = LAZY, optional = false)
    val profile: Profile
) {

    val username: String
        get() = this.profile.username


    val bio: String?
        get() = this.profile.bio

    val image: String?
        get() = this.profile.image

    @Id
    @GeneratedValue
    var id: Long = 0L

}