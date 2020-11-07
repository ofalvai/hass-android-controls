package com.example.hasscontrolsprovider.entity

import java.time.ZonedDateTime

interface HassControl {
    val entityId: String
    val availability: Availability
    val name: String

    /**
     * Textual representation of current state (eg. "on", "23.64 ms", "clear")
     */
    val status: String

    val lastChanged: ZonedDateTime? // null if unavailable
}

enum class Availability {
    AVAILABLE,
    UNAVAILABLE,
    NOT_FOUND, // Removed from Home Assistant, only exists as a previously added control
}

