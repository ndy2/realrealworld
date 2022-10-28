package com.example.realworld.domain.user.service

import com.example.realworld.domain.profile.model.Profile
import com.example.realworld.domain.user.model.User
import com.example.realworld.domain.user.model.inout.Login
import com.example.realworld.domain.user.model.inout.Register
import com.example.realworld.domain.user.model.inout.UserResponse
import com.example.realworld.domain.user.repository.UserRepository
import com.example.realworld.exception.BadCredentialException
import com.example.realworld.exception.NotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val tokenProvider: TokenProvider,
    private val passwordEncoder: PasswordEncoder,
) {

    @Transactional(readOnly = true)
    fun login(login: Login): UserResponse {
        val email = login.email
        val password = login.password

        userRepository.findByEmail(email)?.let {
            if (!passwordEncoder.matches(password, it.password)) {
                throw BadCredentialException()
            }
            return toResponse(it)
        }
        throw NotFoundException("no such user email : $email")

    }

    @Transactional
    fun register(register: Register): UserResponse {
        //createUser
        val user = User(
            register.email,
            passwordEncoder.encode(register.password),
            Profile(register.username)
        )

        //save user
        userRepository.save(user)

        //convert to response
        return toResponse(user)
    }

    private fun toResponse(user: User) = UserResponse(
        user.email,
        user.username,
        user.bio,
        user.image,
        tokenProvider.getToken(user)
    )
}