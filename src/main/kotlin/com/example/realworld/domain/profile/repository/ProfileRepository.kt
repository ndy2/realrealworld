package com.example.realworld.domain.profile.repository

import com.example.realworld.domain.profile.model.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProfileRepository : JpaRepository<Profile, Long>{

    fun findByUsername(username: String): Profile?

    //@formatter:off
    @Query(
        "select p " +
        "from Profile p " +
        "where p.id = :id"
    )
    //@formatter:on
    fun findByProfileId(id: Long): Profile?

    //@formatter:off
    @Query(
        "select p " +
        "from Profile p " +
        "left join fetch p.following fo " +
        "left join fetch p.favorites fa " +
        "where p.id = :id"
    )
    //@formatter:on
    fun findByIdWithFollowingAndFavorite(id: Long): Profile?

    //@formatter:off
    @Query(
        "select p " +
        "from Profile p " +
        "left join fetch p.following f " +
        "where p.id = :profileId"
    )
    //@formatter:on
    fun findByIdWithFollowing(profileId: Long): Profile?
}