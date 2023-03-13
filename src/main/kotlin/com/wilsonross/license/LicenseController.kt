package com.wilsonross.license

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException


@RestController
@RequestMapping("/api/license")
class LicenseController(private val properties: LicenseProperties, private val encryption: Encryption) {

    @PostMapping("/create")
    fun create(@RequestHeader header: Map<String, String>, @RequestBody license: StoredLicense): String {
        // Serialize
        val serializedLicense = Json.encodeToString(license)

        // Encrypt
        val secretKey = this.encryption.generateKeyFromPassword(properties.password, properties.salt)
        return this.encryption.encrypt(serializedLicense, secretKey)
    }

    @PostMapping("/validate")
    fun validate(@RequestBody request: ValidationRequest): ResponseEntity<*> {
        try {
            // Decrypt
            val secretKey = this.encryption.generateKeyFromPassword(properties.password, properties.salt)
            val decrypted = this.encryption.decrypt(request.licenseKey, secretKey)
            val storedLicense = Json.decodeFromString<StoredLicense>(decrypted)
            if (validateName(request, storedLicense)) {
                return ResponseEntity<Any>(HttpStatus.NO_CONTENT)
            }
        } catch (e: IllegalArgumentException) {
            // Decryption error
            return ResponseEntity<Any>(HttpStatus.NOT_FOUND)
        } catch (e: IndexOutOfBoundsException) {
            // Decryption error
            return ResponseEntity<Any>(HttpStatus.NOT_FOUND)
        }

        return ResponseEntity<Any>(HttpStatus.NOT_FOUND)
    }

    /**
     * Validate a request against a license
     * @param request the POST object
     * @param license the license derived from the POST object
     */
    private fun validateName(request: ValidationRequest, license: StoredLicense): Boolean {
        return request.firstName == license.firstName && request.lastName == license.lastName
    }
}