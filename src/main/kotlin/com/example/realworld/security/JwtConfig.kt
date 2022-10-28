package com.example.realworld.security

import com.example.realworld.security.filter.JwtAuthorizationFilter
import com.example.realworld.security.signature.SecuritySigner
import com.example.realworld.security.token.JwtTokenProvider
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jose.jwk.JWK
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfig {

//    @Bean
    fun macJwtAuthorizationFilter(
        @Qualifier("macKey") jwk: JWK
    ): JwtAuthorizationFilter {
        val macVerifier = MACVerifier(jwk.toOctetSequenceKey().toSecretKey())
        return JwtAuthorizationFilter(macVerifier)
    }

        @Bean
    fun rsaJwtAuthorizationFilter(
        @Qualifier("rsaKey") jwk: JWK
    ): JwtAuthorizationFilter {
        val rsaVerifier = RSASSAVerifier(jwk.toRSAKey().toRSAPublicKey())
        return JwtAuthorizationFilter(rsaVerifier)
    }

}