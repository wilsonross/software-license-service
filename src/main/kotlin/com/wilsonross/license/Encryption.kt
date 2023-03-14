package com.wilsonross.license

import org.springframework.stereotype.Component
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

@Component
class Encryption(
    private val random: SecureRandom,
    private val cipher: Cipher,
    private val keyFactory: SecretKeyFactory,
    private val decoder: Base64.Decoder,
    private val encoder: Base64.Encoder
) {
    private val algorithm: String = "AES"
    private val iterationCount: Int = 65536
    private val keyLength: Int = 256

    /**
     * Encrypt a string with a secret key - stores injection vector
     * in first 16 bytes for later use
     */
    fun encrypt(strToEncrypt: String, secretKey: SecretKey): String {
        val iv = this.generateIv()
        val ivSpec = IvParameterSpec(iv)

        this.cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
        val cipherBytes = this.cipher.doFinal(strToEncrypt.toByteArray())
        return this.encoder.encodeToString(iv + cipherBytes)
    }

    /**
     * Decrypt a string with a secret key - assumes first 16 bytes
     * is the injection vector used to encrypt
     */
    fun decrypt(strToDecrypt: String, secretKey: SecretKey): String {
        val decoded = this.decoder.decode(strToDecrypt)
        val iv = decoded.copyOfRange(0, 16)
        val encryptedBytes = decoded.copyOfRange(16, decoded.size)
        val ivSpec = IvParameterSpec(iv)

        this.cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
        val plainBytes = this.cipher.doFinal(encryptedBytes)
        return String(plainBytes)
    }

    fun generateKeyFromPassword(password: String, salt: String): SecretKey {
        val spec = PBEKeySpec(password.toCharArray(), salt.toByteArray(), this.iterationCount, this.keyLength)
        val secret = this.keyFactory.generateSecret(spec).encoded
        return SecretKeySpec(secret, this.algorithm)
    }

    /**
     * Generates a random byte array for use as an injection vector
     */
    private fun generateIv(): ByteArray {
        val iv = ByteArray(16)
        this.random.nextBytes(iv)
        return iv
    }
}