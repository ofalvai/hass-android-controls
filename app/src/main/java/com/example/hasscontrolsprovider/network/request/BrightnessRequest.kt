package com.example.hasscontrolsprovider.network.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BrightnessRequest(
    val entity_id: String,
    val brightness_pct: Int
)