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
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    @Qualifier("rsaJwtAuthorizationFilter")
    private val jwtAuthorizationFilter: JwtAuthorizationFilter,
    private val userDetailsService: UserDetailsService
) {

    @Bean
    fun jwtTokenProvider(
        @Qualifier("rsaSecuritySigner") securitySigner: SecuritySigner,
        @Qualifier("rsaKey") jwk: JWK
    ): JwtTokenProvider {
        return JwtTokenProvider(securitySigner, jwk)
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
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
            .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return NoOpPasswordEncoder.getInstance()
    }
}