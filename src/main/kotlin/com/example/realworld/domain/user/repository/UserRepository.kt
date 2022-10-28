package com.example.realworld.domain.user.repository

import com.example.realworld.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<User, Long> {

    //@formatter:off
    @Query(
        "select u " +
        "from User u " +
        "join fetch u.profile p " +
        "where u.id = :id"
    )
    //@formatter:on
    fun findByUserId(id: Long): User?
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
}