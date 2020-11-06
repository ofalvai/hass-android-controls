package com.example.hasscontrolsprovider.entity

import java.time.ZonedDateTime

interface HassControl {
    val entityId: String
    val availability: Availability
    val name: String
    val status: String
    val lastChanged: ZonedDateTime? // null if unavailable
}

enum class Availability {
    AVAILABLE,
    UNAVAILABLE,
    NOT_FOUND, // Removed from HA, only exists as a control UI
}

