package com.example.realworld.security.signature

import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSSigner
import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import java.util.*


abstract class SecuritySigner {

    @Throws(JOSEException::class)
    protected fun getJwtTokenInternal(
        jwsSigner: JWSSigner,
        user: UserDetails,
        jwk: JWK
    ): String {
        val header = JWSHeader.Builder(jwk.algorithm as JWSAlgorithm).keyID(jwk.keyID).build()



        val jwtClaimsSet = JWTClaimsSet.Builder()
            .subject("user")
            .issuer("http://localhost:8081")
            .claim("username", user.username)
            .claim("authority", AuthorityUtils.authorityListToSet(user.authorities).toList())
            .expirationTime(Date(Date().time + 60 * 1000 * 5))
            .build()
        val signedJWT = SignedJWT(header, jwtClaimsSet)
        signedJWT.sign(jwsSigner)
        return signedJWT.serialize()
    }

    @Throws(JOSEException::class)
    abstract fun getJwtToken(user: UserDetails, jwk: JWK): String
}
