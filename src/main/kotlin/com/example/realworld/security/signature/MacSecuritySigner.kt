package com.example.realworld.security.signature

import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.OctetSequenceKey
import org.springframework.security.core.userdetails.UserDetails

class MacSecuritySigner : SecuritySigner() {

    override fun getJwtToken(user: UserDetails, jwk: JWK): String {
        val jwsSigner = MACSigner((jwk as OctetSequenceKey).toSecretKey())
        return getJwtTokenInternal(jwsSigner, user, jwk)
    }
}