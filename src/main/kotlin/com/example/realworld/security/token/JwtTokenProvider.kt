package com.example.realworld.security.token

import com.example.realworld.domain.user.model.User
import com.example.realworld.domain.user.service.TokenProvider
import com.example.realworld.security.signature.SecuritySigner
import com.nimbusds.jose.jwk.JWK
import org.springframework.security.core.authority.AuthorityUtils

typealias SecurityUser = org.springframework.security.core.userdetails.User

class JwtTokenProvider(
    private val securitySigner: SecuritySigner,
    private val jwk: JWK
) : TokenProvider {

    override fun getToken(user: User): String {
        val securityUser = SecurityUser(
            user.id.toString(),
            user.password,
            AuthorityUtils.createAuthorityList("user")
        )

        return securitySigner.getJwtToken(securityUser, jwk)
    }
}