package com.example.realworld.domain.profile.service

import com.example.realworld.domain.profile.model.inout.ProfileResponse
import com.example.realworld.domain.profile.repository.ProfileRepository
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service

@Service
class ProfileService(
    private val repository: ProfileRepository
) {

    fun getByUsername(currentUserProfileId: Long, username: String): ProfileResponse {
        repository.findByUsername(username)?.let {
            return ProfileResponse(
                it.username,
                it.bio,
                it.image,
                it.followingProfileId(currentUserProfileId)
            )
        }
        throw NotFoundException()
    }
}