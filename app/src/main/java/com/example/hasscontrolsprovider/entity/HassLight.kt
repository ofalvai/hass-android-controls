package com.example.hasscontrolsprovider.entity

import java.time.ZonedDateTime

data class HassLight(
    override val entityId: String,
    override val availability: Availability,
    override val name: String,
    override val status: String,
    override val lastChanged: ZonedDateTime?,
    val state: Boolean,
    val features: Set<LightFeatures>,
    val brightness: Int,
    val colorTemp: Int
) : HassControl {
    val brightnessPercent: Float
        get() = brightness / 255f * 100
}

enum class LightFeatures {
    BRIGHTNESS,
    COLOR_TEMP,
    COLOR,
    WHITE_VALUE
}