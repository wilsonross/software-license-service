package com.wilsonross.license

class CreateBody(
    val firstName: String,
    val lastName: String,
    val softwarePackage: String,
    val secret: String
)

class ValidateBody(
    val firstName: String,
    val lastName: String,
    val licenseKey: String,
)