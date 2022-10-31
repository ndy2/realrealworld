package com.example.realworld.domain.profile.repository

import com.example.realworld.domain.profile.model.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProfileRepository : JpaRepository<Profile, Long> {

    //@formatter:off
    @Query(
        "select p " +
        "from Profile p " +
        "where p.id = :id"
    )
    //@formatter:on
    fun findByProfileId(id: Long): Profile?

    fun findByUsername(username: String): Profile?


    //@formatter:off
    @Query(
        "select p " +
        "from Profile p " +
        "join fetch p.following f " +
        "where p.username = :username"
    )
    //@formatter:on
    fun findByUsernameWithFollowing(username: String): Profile?
}