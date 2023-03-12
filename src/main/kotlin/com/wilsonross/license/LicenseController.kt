package com.wilsonross.license

import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/license")
class LicenseController(private val properties: LicenseProperties, private val encryption: Encryption) {

    @PostMapping("/create")
    fun create(@RequestBody createBody: CreateBody): String {
        // Serialize
        val license = StoredLicense(createBody.firstName, createBody.lastName, createBody.softwarePackage)
        val serializedLicense = Json.encodeToString(license)

        // Encrypt
        val secretKey = this.encryption.generateKeyFromPassword(properties.password, properties.salt)
        return this.encryption.encrypt(serializedLicense, secretKey)
    }

    @PostMapping("/validate")
    fun validate(@RequestBody validateBody: ValidateBody): String {
        // Decrypt
        val secretKey = this.encryption.generateKeyFromPassword(properties.password, properties.salt)
        val decrypted = this.encryption.decrypt(validateBody.licenseKey, secretKey)

        // Deserialize
        val storedLicense = Json.decodeFromString<StoredLicense>(decrypted)
        return storedLicense.firstName
    }
}