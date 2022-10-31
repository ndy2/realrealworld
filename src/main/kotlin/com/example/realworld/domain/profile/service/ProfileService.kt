package com.example.realworld.domain.profile.service

import com.example.realworld.domain.profile.model.Profile
import com.example.realworld.domain.profile.model.inout.ProfileResponse
import com.example.realworld.domain.profile.repository.ProfileRepository
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProfileService(
    private val repository: ProfileRepository
) {

    fun getByUsername(currentUserProfileId: Long, username: String): ProfileResponse {
        val currentUserProfile = getCurrentUserProfile(currentUserProfileId)

        repository.findByUsername(username)?.let {
            return ProfileResponse(
                it.username,
                it.bio,
                it.image,
                it.isFollowing(currentUserProfile)
            )
        }
        throw NotFoundException()
    }

    @Transactional
    fun followOrUnfollow(currentUserProfileId: Long, username: String): ProfileResponse {
        val currentUserProfile = getCurrentUserProfile(currentUserProfileId)

        repository.findByUsername(username)?.let {
            currentUserProfile.followOrUnfollow(it)
            return ProfileResponse(
                it.username,
                it.bio,
                it.image,
                currentUserProfile.isFollowing(it)
            )
        }
        throw NotFoundException()
    }

    private fun getCurrentUserProfile(currentUserProfileId: Long) : Profile {
        return repository.findByProfileId(currentUserProfileId) ?: throw NotFoundException()
    }
}