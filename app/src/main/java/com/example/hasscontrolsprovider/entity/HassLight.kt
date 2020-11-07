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

    /**
     * Brightness in the range of 0..255, 0 if unsupported
     */
    val brightness: Int,

    /**
     * Minimum supported color temperature in Mired unit, 0 if unsupported
     */
    val minColorTemp: Int = 0,

    /**
     * Maximum supported color temperature in Mired unit, 0 if unsupported
     */
    val maxColorTemp: Int = 0,

    /**
     * Color temperature in Mired unit in the range of [minColorTemp]..[maxColorTemp], 0 if unsupported
     */
    val colorTemp: Int = 0

) : HassControl {

    val brightnessPercent: Float
        get() = brightness / MAX_BRIGHTNESS.toFloat() * 100

    companion object {
        const val MIN_BRIGHTNESS = 0
        const val MAX_BRIGHTNESS = 255
    }
}

enum class LightFeatures {
    BRIGHTNESS,
    COLOR_TEMP,
    COLOR,
    WHITE_VALUE
}