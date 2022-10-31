package com.example.realworld.security

import com.example.realworld.security.token.CustomJwtGrantedAuthoritiesConverter
import com.example.realworld.security.token.JwtTokenProvider
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.*
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import java.io.FileInputStream
import java.security.*
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*

@Configuration
class SecurityConfig(
    private val userDetailsService: UserDetailsService
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        val authenticationConverter = JwtAuthenticationConverter()
        authenticationConverter.setJwtGrantedAuthoritiesConverter(CustomJwtGrantedAuthoritiesConverter())

        return http
            .csrf().disable()
            .cors().disable()
            .httpBasic().disable()
            .headers().frameOptions().disable()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/", "/h2-console/**", "/error").permitAll()
            .antMatchers(HttpMethod.POST, "/api/users", "/api/users/login").permitAll()
            .anyRequest().authenticated()
            .and()
            .userDetailsService(userDetailsService)
            .oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(authenticationConverter)
            .and()
            .and()
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return NoOpPasswordEncoder.getInstance()
    }

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
        val path = "/Users/deukyun/Desktop/realworld/src/main/resources/certs/"
        val publicKeyInputStream = FileInputStream(path + "apiKey.jks")
        val alias = "apiKey"
        val password = "pass1234".toCharArray()

        val keystore = KeyStore.getInstance(KeyStore.getDefaultType() /* = "jks" */)
        keystore.load(publicKeyInputStream, password)

        publicKeyInputStream.close()
        return KeyPair(
            keystore.getCertificate(alias).publicKey,
            keystore.getKey(alias, password) as PrivateKey
        )
    }

}