package com.example.hasscontrolsprovider.network.response

import com.squareup.moshi.JsonClass
import java.time.ZonedDateTime

@JsonClass(generateAdapter = true)
data class HassState(
    val entity_id: String,
    val state: String,
    val attributes: Attributes,
    val last_changed: ZonedDateTime?
) {

    companion object {
        const val STATE_ON = "on"
        const val STATE_OFF = "off"
        const val STATE_UNAVAILABLE = "unavailable"
        const val STATE_NOT_FOUND = "not_found" // Used for leftover UI controls
        const val STATE_PLAYING = "playing"
        const val STATE_PAUSED = "paused"
        const val STATE_IDLE = "idle"

        fun notFoundState(id: String) = HassState(
            id,
            STATE_NOT_FOUND,
            Attributes(),
            null
        )
    }

    @JsonClass(generateAdapter = true)
    data class Attributes(
        val friendly_name: String? = null,
        val brightness: Int? = null,
        val supported_features: Int? = null,
        val color_temp: Int? = null,
        val min_mireds: Int? = null,
        val max_mireds: Int? = null,
        val battery_level: Float? = null,
        val volume_level: Float? = null,
        val media_title: String? = null,
        val media_artist: String? = null
    )

    val entityType: EntityType = entity_id.toEntityType()
}


enum class EntityType {
    UNKNOWN,
    LIGHT,
    SWITCH,
    VACUUM,
    CAMERA,
    MEDIA_PLAYER
}

object SupportedLightFeatureFlags {
    const val BRIGHTNESS = 1
    const val COLOR_TEMP = 2
    const val COLOR = 16
    const val WHITE_VALUE = 128 // TODO
}

object SupportedVacuumFeatureFlags {
    const val BATTERY = 64
}

private fun String.toEntityType(): EntityType {
    return when {
        startsWith("light.") -> EntityType.LIGHT
        startsWith("camera.") -> EntityType.CAMERA
        startsWith("vacuum.") -> EntityType.VACUUM
        startsWith("switch.") -> EntityType.SWITCH
        startsWith("media_player.") -> EntityType.MEDIA_PLAYER
        else -> EntityType.UNKNOWN
    }
}