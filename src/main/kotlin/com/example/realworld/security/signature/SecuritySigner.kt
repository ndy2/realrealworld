package com.example.realworld.security.signature

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSSigner
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import org.springframework.security.core.userdetails.UserDetails
import java.util.*


open class SecuritySigner(
    var jwsSigner: JWSSigner? = null
) {
    fun getJwtToken(user: UserDetails, jwk: JWK): String {
        return getJwtTokenInternal(jwsSigner!!, user, jwk)
    }

    private fun getJwtTokenInternal(
        jwsSigner: JWSSigner,
        user: UserDetails,
        jwk: JWK
    ): String {
        val header = JWSHeader.Builder(jwk.algorithm as JWSAlgorithm).keyID(jwk.keyID).build()

        val jwtClaimsSet = JWTClaimsSet.Builder()
            .subject("userId")
            .issuer("REALWORLD API")
            .issueTime(Date())
            .expirationTime(Date(System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000)) // 10 days
            .claim("userId", user.username)
            .claim("role", user.authorities.joinToString { it.authority })
            .claim("scope", "photo")
            .build()
        val signedJWT = SignedJWT(header, jwtClaimsSet)
        signedJWT.sign(jwsSigner)
        return signedJWT.serialize()
    }
}
