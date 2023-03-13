package com.wilsonross.license

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.security.SecureRandom
import java.util.*
import java.util.Base64.Decoder
import java.util.Base64.Encoder
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory

@Configuration
@EnableWebSecurity
class Config(private val properties: LicenseProperties) {
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

    @Bean
    fun authorizationFilter(): AuthorizationFilter {
        return AuthorizationFilter()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        /*http.csrf().disable().authorizeHttpRequests().requestMatchers(HttpMethod.POST, "/api/license/validate")
            .permitAll().anyRequest().authenticated().and()
            .addFilter(BasicAuthenticationFilter(AuthenticationManager())).sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)*/
        http.cors().disable().csrf().disable().addFilterAfter(authorizationFilter(), BasicAuthenticationFilter::class.java)

        return http.build()
    }
}