package com.example.realworld.security.token

import com.example.realworld.domain.user.model.User
import com.example.realworld.domain.user.service.TokenProvider
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit

@Component
class JwtTokenProvider(
    private val jwtEncoder: JwtEncoder
) : TokenProvider {

    override fun getToken(user: User): String {
        val headers = JwsHeader.with(SignatureAlgorithm.RS256).build()
        val claims = JwtClaimsSet.builder()
            .issuer("REALWORLD API")
            .subject("userId")
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plus(5L, ChronoUnit.MINUTES))
            .claim("userId", user.id)
            .claim("role", "user")
            .claim("scope", "photo").build()

        val jwt = jwtEncoder.encode(JwtEncoderParameters.from(headers, claims))
        return jwt.tokenValue
    }
}