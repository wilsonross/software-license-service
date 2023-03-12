package com.wilsonross.license

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(LicenseProperties::class)
class LicenseApplication

fun main(args: Array<String>) {
	runApplication<LicenseApplication>(*args)
}
