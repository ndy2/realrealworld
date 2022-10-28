package com.example.realworld.security

import com.example.realworld.security.filter.JwtAuthorizationMacFilter
import com.example.realworld.security.signature.SecuritySigner
import com.example.realworld.security.token.JwtTokenProvider
import com.nimbusds.jose.jwk.JWK
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfig {

    @Bean
    fun jwtTokenProvider(securitySigner: SecuritySigner, jwk: JWK): JwtTokenProvider {
        return JwtTokenProvider(securitySigner, jwk)
    }

    @Bean
    fun jwtAuthorizationMacFilter(jwk: JWK): JwtAuthorizationMacFilter {
        return JwtAuthorizationMacFilter(jwk)
    }
}