package com.wilsonross.license

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.util.Assert
import java.util.*


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LicenseApplicationTests(
    @Autowired private val restTemplate: TestRestTemplate,
    @Autowired private val properties: LicenseProperties
) {
    private val createUrl = "/api/license/create"
    private val validateUrl = "/api/license/validate"

    @Test
    fun `Assert that license key is created with successful authorization`() {
        val bearer = Base64.getEncoder().encodeToString(properties.token.toByteArray())
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $bearer")

        val license = StoredLicense("firstName", "lastName", "spring")
        val request = HttpEntity<StoredLicense>(license, headers)

        val result = restTemplate.postForEntity<String>(createUrl, request)
        Assert.isTrue(result.body!!.length == 128, "should have a specific length")
    }

    @Test
    fun `Assert that 204 response returned on successful license validation`() {
        val license =
            "fZPZZJyfogHXp1BGbeeAToEud7HvisLfF/OIwApmnDHi8NobAg6tk2ij+EaDMJkWzHoHczjWuCTC9cIiY8egasDE8i+9ka1Ia447Povy5DavbC9Gw8FIk2jUcwPDxEJu"

        val validation = ValidationRequest("firstName", "lastName", license)
        val request = HttpEntity<ValidationRequest>(validation)
        val result = restTemplate.postForEntity<Any>(validateUrl, request)
        Assert.isTrue(result.statusCode == HttpStatus.NO_CONTENT, "result must have successful status code")
    }

    @Test
    fun `Assert that 404 response returned on unsuccessful license validation`() {
        val validation = ValidationRequest("firstName", "lastName", "dGVzNDjWNodA==")
        val request = HttpEntity<ValidationRequest>(validation)
        val result = restTemplate.postForEntity<Any>(validateUrl, request)
        Assert.isTrue(result.statusCode == HttpStatus.NOT_FOUND, "result must have a not found status code")
    }
}
