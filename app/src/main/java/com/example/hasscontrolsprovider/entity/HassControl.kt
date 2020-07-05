package com.example.hasscontrolsprovider.entity

interface HassControl {
    val entityId: String
    val availability: Availability
    val name: String
    val status: String
}

enum class Availability {
    AVAILABLE,
    UNAVAILABLE,
    NOT_FOUND, // Removed from HA, only exists as a control UI
}

