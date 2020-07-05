package com.example.hasscontrolsprovider.mapper

import com.example.hasscontrolsprovider.entity.*
import com.example.hasscontrolsprovider.network.response.EntityType
import com.example.hasscontrolsprovider.network.response.HassState
import com.example.hasscontrolsprovider.network.response.SupportedLightFeatureFlags
import com.example.hasscontrolsprovider.network.response.SupportedVacuumFeatureFlags
import kotlin.math.roundToInt

fun HassState.toHassControl(): HassControl {
    return when (this.entityType) {
        EntityType.LIGHT -> toLight()
        EntityType.SWITCH -> toSwitch()
        EntityType.CAMERA -> toCamera()
        EntityType.VACUUM -> toVacuum()
        else -> throw IllegalArgumentException("Unsupported entity type: $entityType")
    }
}

private fun HassState.toLight(): HassLight {
    return HassLight(
        entityId = entity_id,
        isAvailable = state != HassState.STATE_UNAVAILABLE,
        name = attributes.friendly_name ?: entity_id,
        status = state,
        state = state == HassState.STATE_ON,
        features = parseSupportedLightFeatures(this),
        brightness = attributes.brightness ?: 0,
        colorTemp = attributes.color_temp ?: 0
    )
}

private fun HassState.toSwitch(): HassSwitch {
    return HassSwitch(
        entityId = entity_id,
        isAvailable = state != HassState.STATE_UNAVAILABLE,
        name = attributes.friendly_name ?: entity_id,
        status = state,
        enabled = state == HassState.STATE_ON
    )
}

private fun HassState.toCamera(): HassCamera {
    return HassCamera(
        entityId = entity_id,
        isAvailable = state != HassState.STATE_UNAVAILABLE,
        name = attributes.friendly_name ?: entity_id,
        status = state
    )
}

private fun HassState.toVacuum(): HassVacuum {
    return HassVacuum(
        entityId = entity_id,
        isAvailable = state != HassState.STATE_UNAVAILABLE,
        name = attributes.friendly_name ?: entity_id,
        status = state,
        features = parseSupportedVacuumFeatures(this),
        batteryPercent = attributes.battery_level?.roundToInt() ?: 0
    )
}

private fun parseSupportedLightFeatures(hassState: HassState): Set<LightFeatures> {
    val featureInt = hassState.attributes.supported_features ?: return emptySet()

    val features = mutableSetOf<LightFeatures>()
    if (SupportedLightFeatureFlags.BRIGHTNESS and featureInt > 0) {
        features.add(LightFeatures.BRIGHTNESS)
    }
    if (SupportedLightFeatureFlags.COLOR_TEMP and featureInt > 0) {
        features.add(LightFeatures.COLOR_TEMP)
    }
    if (SupportedLightFeatureFlags.COLOR and featureInt > 0) {
        features.add(LightFeatures.COLOR)
    }
    if (SupportedLightFeatureFlags.WHITE_VALUE and featureInt > 0) {
        features.add(LightFeatures.WHITE_VALUE)
    }

    return features
}

private fun parseSupportedVacuumFeatures(hassState: HassState): Set<VacuumFeatures> {
    val featureInt = hassState.attributes.supported_features ?: return emptySet()

    val features = mutableSetOf<VacuumFeatures>()
    if (SupportedVacuumFeatureFlags.BATTERY and featureInt > 0) {
        features.add(VacuumFeatures.BATTERY_PERCENT)
    }

    return features
}