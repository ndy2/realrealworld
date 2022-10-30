package com.example.realworld.init

import com.example.realworld.security.signature.RsaPublicKeySecuritySigner
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.security.*;
import java.security.cert.Certificate;
import java.util.*

@Component
class RsaKeyExtractor : ApplicationRunner {

    @Autowired
    private val rsaPublicKeySecuritySigner: RsaPublicKeySecuritySigner? = null

    override fun run(args: ApplicationArguments) {
        val path = "/Users/deukyun/Desktop/realworld/src/main/resources/certs/"
        val file = File(path + "publicKey.txt")
        val `is` = FileInputStream(path + "apiKey.jks")
        val keystore = KeyStore.getInstance(KeyStore.getDefaultType())
        keystore.load(`is`, "pass1234".toCharArray())
        val alias = "apiKey"
        val key: Key = keystore.getKey(alias, "pass1234".toCharArray())
        if (key is PrivateKey) {
            val certificate: Certificate = keystore.getCertificate(alias)
            val publicKey: PublicKey = certificate.publicKey
            val keyPair = KeyPair(publicKey, key)
            rsaPublicKeySecuritySigner?.setPrivateKey(keyPair.private)
            if (!file.exists()) {
                var publicStr = Base64.getMimeEncoder().encodeToString(publicKey.encoded)
                publicStr = "-----BEGIN PUBLIC KEY-----\r\n$publicStr\r\n-----END PUBLIC KEY-----"
                val writer = OutputStreamWriter(FileOutputStream(file), Charset.defaultCharset())
                writer.write(publicStr)
                writer.close()
            }
        }
        `is`.close()
    }
}