package com.example.realworld.security

import com.example.realworld.security.signature.MacSecuritySigner
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.OctetSequenceKey
import com.nimbusds.jose.jwk.gen.OctetSequenceKeyGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SignatureConfig {

    @Bean
    fun macSecuritySigner(): MacSecuritySigner {
        return MacSecuritySigner()
    }

    @Bean
    fun octetSequenceKey(): OctetSequenceKey {
        return OctetSequenceKeyGenerator(256)
            .keyID("macKey")
            .algorithm(JWSAlgorithm.HS256)
            .generate()
    }
}