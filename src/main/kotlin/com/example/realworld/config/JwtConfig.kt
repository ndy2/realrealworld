package com.example.realworld.config

import com.example.realworld.config.properties.JwtProperties
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.*
import java.io.FileInputStream
import java.security.KeyPair
import java.security.KeyStore
import java.security.PrivateKey
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*

@Configuration
class JwtConfig(
    private val jwtProperties: JwtProperties
) {


    @Bean
    fun jwtDecoder(): JwtDecoder? {
        val keyPair = generateRsaKeyPair()
        val publicKey = keyPair.public as RSAPublicKey

        val jwtDecoder = NimbusJwtDecoder.withPublicKey(publicKey)
            .signatureAlgorithm(SignatureAlgorithm.from("RS256")).build()
        jwtDecoder.setJwtValidator(JwtValidators.createDefault())
        return jwtDecoder
    }

    @Bean
    fun jwtEncoder(): JwtEncoder {
        val rsaKey: RSAKey = generateRsaKey()
        val jwkSet = JWKSet(rsaKey)

        return NimbusJwtEncoder { jwkSelector, _ -> jwkSelector.select(jwkSet) }
    }

    private fun generateRsaKey(): RSAKey {
        val keyPair = generateRsaKeyPair()
        val publicKey = keyPair.public as RSAPublicKey
        val privateKey = keyPair.private as RSAPrivateKey
        return RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString())
            .build()
    }

    private fun generateRsaKeyPair(): KeyPair {
        val publicKeyInputStream = ClassLoader.getSystemResourceAsStream(jwtProperties.publicKey)!!
        val alias = jwtProperties.alias
        val password = jwtProperties.password.toCharArray()

        val keystore = KeyStore.getInstance(KeyStore.getDefaultType() /* = "jks" */)
        keystore.load(publicKeyInputStream, password)

        publicKeyInputStream.close()
        return KeyPair(
            keystore.getCertificate(alias).publicKey,
            keystore.getKey(alias, password) as PrivateKey
        )
    }
}