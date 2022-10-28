package com.example.realworld.domain.user.service

import com.example.realworld.domain.user.model.inout.UpdateUser
import com.example.realworld.domain.user.model.inout.UserResponse
import com.example.realworld.domain.user.repository.UserRepository
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val repository: UserRepository,
    private val tokenProvider: TokenProvider
) {

    @Transactional(readOnly = true)
    fun getById(userId: Long): UserResponse {

        repository.findByUserId(userId)?.let {
            return UserResponse(
                it.email,
                it.username,
                it.bio,
                it.image
            )
        }
        throw NotFoundException()
    }

    @Transactional
    fun update(userId: Long, updateUser: UpdateUser): UserResponse {
        val (email, password, username, image, bio) = updateUser

        repository.findByUserId(userId)?.let {
            it.update(email, password, username, image, bio)
            return UserResponse(it.email, it.username, it.bio, it.image)
        }
        throw NotFoundException()
    }

}