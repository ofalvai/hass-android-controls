package com.example.hasscontrolsprovider.network.request

data class BrightnessRequest(
    val entity_id: String,
    val brightness_pct: Int
)