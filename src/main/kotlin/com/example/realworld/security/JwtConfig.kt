package com.example.realworld.security

import com.example.realworld.security.filter.JwtAuthorizationFilter
import com.example.realworld.security.signature.SecuritySigner
import com.example.realworld.security.token.JwtTokenProvider
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jose.jwk.JWK
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder

@Configuration
class JwtConfig {

    @Bean
    @ConditionalOnProperty(
        prefix = "spring.security.oauth2.resourceserver.jwt",
        name = ["jws-algorithms"],
        havingValue = "HS256"
    )
    fun macJwtAuthorizationFilter(jwk: JWK): JwtAuthorizationFilter {
        val macVerifier = MACVerifier(jwk.toOctetSequenceKey().toSecretKey())
        return JwtAuthorizationFilter(macVerifier)
    }

    @Bean
    @ConditionalOnProperty(
        prefix = "spring.security.oauth2.resourceserver.jwt",
        name = ["jws-algorithms"],
        havingValue = "HS256"
    )
    fun macJwdDecoder(macKey: JWK): JwtDecoder {
        return NimbusJwtDecoder.withSecretKey(macKey.toOctetSequenceKey().toSecretKey())
            .macAlgorithm(MacAlgorithm.HS256)
            .build()
    }

    @Bean
    @ConditionalOnProperty(
        prefix = "spring.security.oauth2.resourceserver.jwt",
        name = ["jws-algorithms"],
        havingValue = "RS256"
    )
    fun rsaJwtAuthorizationFilter(jwk: JWK): JwtAuthorizationFilter {
        val rsaVerifier = RSASSAVerifier(jwk.toRSAKey().toRSAPublicKey())
        return JwtAuthorizationFilter(rsaVerifier)
    }

    @Bean
    @ConditionalOnProperty(
        prefix = "spring.security.oauth2.resourceserver.jwt",
        name = ["jws-algorithms"],
        havingValue = "RS256"
    )
    fun rsaDecoder(rsaKey: JWK): JwtDecoder {
        return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAKey().toRSAPublicKey())
            .signatureAlgorithm(SignatureAlgorithm.RS256)
            .build()
    }
}