package com.example.realworld.security.service

import com.example.realworld.domain.user.repository.UserRepository
import com.example.realworld.exception.NotFoundException
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    /* load user by email */
    override fun loadUserByUsername(email: String): UserDetails {
        userRepository.findByEmail(email)?.let {
            return User(
                email,
                it.password,
                AuthorityUtils.createAuthorityList("ROLE_USER")
            )
        }
        throw NotFoundException("no such user email : $email")
    }
}