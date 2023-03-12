package com.wilsonross.license

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.security.SecureRandom
import java.util.*
import java.util.Base64.Decoder
import java.util.Base64.Encoder
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory

@Configuration
class Config {
    @Bean
    fun secureRandom(): SecureRandom {
        return SecureRandom()
    }

    @Bean
    fun cipher(): Cipher {
        return Cipher.getInstance("AES/CBC/PKCS5Padding")
    }

    @Bean
    fun secretKeyFactory(): SecretKeyFactory {
        return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
    }

    @Bean
    fun decoder(): Decoder {
        return Base64.getDecoder()
    }

    @Bean
    fun encoder(): Encoder {
        return Base64.getEncoder()
    }
}