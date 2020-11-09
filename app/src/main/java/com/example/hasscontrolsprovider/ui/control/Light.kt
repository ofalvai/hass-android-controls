package com.example.hasscontrolsprovider.ui.control

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.ui.tooling.preview.Preview
import com.example.hasscontrolsprovider.R
import com.example.hasscontrolsprovider.entity.Availability
import com.example.hasscontrolsprovider.entity.HassLight
import com.example.hasscontrolsprovider.entity.LightFeatures
import com.example.hasscontrolsprovider.ui.HassTheme
import com.example.hasscontrolsprovider.ui.horizontalGradientBackground
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
            ColorTempSlider(
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

@Composable
fun ColorTempSlider(
    label: String,
    @DrawableRes iconRes: Int,
    value: Float,
    valueRange: IntRange,
    onValueChange: (Float) -> Unit
) {
    val gradientColors = remember {
        listOf(
            Color(166, 209, 255),
            Color.White,
            Color(255, 160, 0)
        )
    }

    Text(
        text = label,
        style = MaterialTheme.typography.caption,
        modifier = Modifier.padding(top = 16.dp)
    )
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            asset = vectorResource(iconRes),
            tint = iconDefaultColor
        )
        Box(Modifier
            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .horizontalGradientBackground(gradientColors)
        ) {
            // We need to wrap the Slider in a Box to apply a gradient background modifier
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = valueRange.first.toFloat()..valueRange.last.toFloat()
            )
        }
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