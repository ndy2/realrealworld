package com.example.realworld.security.signature

import com.nimbusds.jose.crypto.RSASSASigner
import org.springframework.stereotype.Component
import java.security.PrivateKey

@Component
class RsaPublicKeySecuritySigner : SecuritySigner() {

    fun setPrivateKey(privateKey: PrivateKey?) {
        super.jwsSigner = RSASSASigner(privateKey)
    }
}