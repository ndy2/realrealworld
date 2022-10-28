package com.example.realworld.security

import com.example.realworld.security.signature.SecuritySigner
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.OctetSequenceKey
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.gen.OctetSequenceKeyGenerator
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SignatureConfig {

    @Bean
    @ConditionalOnProperty(
        prefix = "spring.security.oauth2.resourceserver.jwt",
        name = ["jws-algorithms"],
        havingValue = "HS256"
    )
    fun macSecuritySigner(@Qualifier("macKey") jwk: JWK): SecuritySigner {
        val jwsSigner = MACSigner((jwk as OctetSequenceKey).toSecretKey())
        return SecuritySigner(jwsSigner)
    }

    @Bean
    @ConditionalOnProperty(
        prefix = "spring.security.oauth2.resourceserver.jwt",
        name = ["jws-algorithms"],
        havingValue = "HS256"
    )
    fun macKey(): OctetSequenceKey {
        return OctetSequenceKeyGenerator(256)
            .keyID("macKey")
            .algorithm(JWSAlgorithm.HS256)
            .generate()
    }

    @Bean
    @ConditionalOnProperty(
        prefix = "spring.security.oauth2.resourceserver.jwt",
        name = ["jws-algorithms"],
        havingValue = "RS256"
    )
    fun rsaSecuritySigner(@Qualifier("rsaKey") jwk: JWK): SecuritySigner {
        val rsaSigner = RSASSASigner((jwk as RSAKey).toRSAPrivateKey())
        return SecuritySigner(rsaSigner)
    }

    @Bean
    @ConditionalOnProperty(
        prefix = "spring.security.oauth2.resourceserver.jwt",
        name = ["jws-algorithms"],
        havingValue = "RS256"
    )
    fun rsaKey(): RSAKey {
        return RSAKeyGenerator(2048)
            .keyID("rsaKey")
            .algorithm(JWSAlgorithm.RS256)
            .generate()
    }
}