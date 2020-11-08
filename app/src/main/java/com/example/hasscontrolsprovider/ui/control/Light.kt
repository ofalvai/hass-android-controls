package com.example.hasscontrolsprovider.ui.control

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.ui.tooling.preview.Preview
import com.example.hasscontrolsprovider.R
import com.example.hasscontrolsprovider.entity.Availability
import com.example.hasscontrolsprovider.entity.HassLight
import com.example.hasscontrolsprovider.entity.LightFeatures
import com.example.hasscontrolsprovider.ui.HassTheme
import com.example.hasscontrolsprovider.ui.iconDefaultColor
import com.example.hasscontrolsprovider.ui.iconEnabledColor
import java.time.ZonedDateTime
import kotlin.math.roundToInt

@Composable
fun LightControl(
    control: LiveData<HassLight>,
    onToggle: (Boolean) -> Unit,
    onBrightnessChange: (Float) -> Unit,
    onColorTempChange: (Float) -> Unit
) {
    val controlState by control.observeAsState(initialLightState)

    Control {
        val iconColor = if (controlState.state) iconEnabledColor else iconDefaultColor
        EntityInfo(controlState, iconColor) {
            Switch(
                checked = controlState.state,
                onCheckedChange = { onToggle(it) },
                color = MaterialTheme.colors.primary
            )
        }

        if (controlState.features.contains(LightFeatures.BRIGHTNESS)) {
            LabeledSlider(
                label = "Brightness",
                iconRes = R.drawable.ic_brightness,
                value = controlState.brightness.toFloat(),
                valueRange = HassLight.MIN_BRIGHTNESS..HassLight.MAX_BRIGHTNESS,
                onValueChange = onBrightnessChange
            )
        }

        if (controlState.features.contains(LightFeatures.COLOR_TEMP)) {
            LabeledSlider(
                label = "Color temperature",
                iconRes = R.drawable.ic_color_temp,
                value = controlState.colorTemp.toFloat(),
                valueRange = controlState.minColorTemp..controlState.maxColorTemp,
                onValueChange = onColorTempChange
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        EntityAttribute(name = "State", value = if (controlState.state) "ON" else "OFF")
        EntityAttribute(name = "Brightness", value = "${controlState.brightnessPercent.toInt()}%")
        EntityAttribute(name = "Color temperature", value = "${controlState.colorTemp} mireds")
    }
}

@Preview(name = "Light")
@Composable
fun PreviewLightControl() {
    val light = HassLight(
        entityId = "light.living_room",
        availability = Availability.AVAILABLE,
        name = "Living Room",
        state = true,
        lastChanged = ZonedDateTime.now().minusMinutes(6),
        status = "on",
        features = setOf(LightFeatures.BRIGHTNESS, LightFeatures.COLOR_TEMP),
        brightness = 50,
        colorTemp = 250,
        minColorTemp = 153,
        maxColorTemp = 370
    )
    val liveData = MutableLiveData(light)

    HassTheme {
        LightControl(
            liveData,
            onToggle = { liveData.value = liveData.value!!.copy(state = it) },
            onBrightnessChange = {
                liveData.value = liveData.value!!.copy(brightness = it.roundToInt())
            },
            onColorTempChange = {
                liveData.value = liveData.value!!.copy(colorTemp = it.roundToInt())
            }
        )
    }
}

private val initialLightState = HassLight(
    entityId = "",
    availability = Availability.UNAVAILABLE,
    name = "",
    status = "unavailable",
    lastChanged = null,
    state = false,
    features = emptySet(),
    brightness = 0,
    colorTemp = 0
)