package com.example.realworld.security

import com.example.realworld.security.filter.JwtAuthorizationFilter
import com.example.realworld.security.signature.SecuritySigner
import com.example.realworld.security.token.CustomJwtGrantedAuthoritiesConverter
import com.example.realworld.security.token.JwtTokenProvider
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val userDetailsService: UserDetailsService
) {

    @Bean
    fun rsaKey(): RSAKey {
        return RSAKeyGenerator(2048)
            .keyID("rsaKey")
            .algorithm(JWSAlgorithm.RS256)
            .generate()
    }

    @Bean
    fun jwtTokenProvider(
        securitySigner: SecuritySigner,
        jwk: JWK
    ): JwtTokenProvider {
        return JwtTokenProvider(securitySigner, jwk)
    }

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
}