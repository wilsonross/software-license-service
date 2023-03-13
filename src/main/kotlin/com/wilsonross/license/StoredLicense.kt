package com.wilsonross.license

import kotlinx.serialization.Serializable

@Serializable
class StoredLicense(val firstName: String, val lastName: String, val softwarePackage: String)

