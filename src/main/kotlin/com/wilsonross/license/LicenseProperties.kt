package com.wilsonross.license

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("license")
class LicenseProperties(val password: String, val salt: String, val token: String)