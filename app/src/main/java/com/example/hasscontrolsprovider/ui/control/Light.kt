package com.example.hasscontrolsprovider.ui.control

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import java.time.ZonedDateTime

@Composable
fun Light(
    control: LiveData<HassLight>,
    onToggle: (Boolean) -> Unit,
    onBrightnessChange: (Float) -> Unit,
    onColorTempChange: (Float) -> Unit
) {
    val controlState by control.observeAsState(initialLightState)

    Column(modifier = Modifier.padding(16.dp)) {
        EntityInfo(controlState) {
            Switch(
                checked = controlState.state,
                onCheckedChange = { onToggle(it) }
            )
        }

        if (controlState.features.contains(LightFeatures.BRIGHTNESS)) {
            SliderControl(
                label = "Brightness",
                iconRes = R.drawable.ic_brightness,
                value = controlState.brightnessPercent,
                onValueChange = onBrightnessChange
            )
        }

        if (controlState.features.contains(LightFeatures.COLOR_TEMP)) {
            SliderControl(
                label = "Color temperature",
                iconRes = R.drawable.ic_color_temp,
                value = controlState.colorTempPercent,
                onValueChange = onColorTempChange
            )
        }

        Divider(color = Color.Gray)
        EntityAttribute(name = "State", value = if (controlState.state) "ON" else "OFF")
        EntityAttribute(name = "Brightness", value = "${controlState.brightnessPercent.toInt()}%")
    }
}

@Composable
private fun SliderControl(
    label: String,
    @DrawableRes iconRes: Int,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Text(
        text = label,
        style = MaterialTheme.typography.caption
    )
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(vectorResource(iconRes))
        Slider(
            modifier = Modifier.padding(start = 16.dp),
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..100f
        )
    }
}

@Preview(widthDp = 300, showBackground = true)
@Composable
private fun PreviewLightEntity() {
    val light = HassLight(
        entityId = "light.living_room",
        availability = Availability.AVAILABLE,
        name = "Living Room",
        state = true,
        lastChanged = ZonedDateTime.now().minusMinutes(6),
        status = "on",
        features = setOf(LightFeatures.BRIGHTNESS, LightFeatures.COLOR_TEMP),
        brightness = 50,
        colorTemp = 0
    )
    val liveData = MutableLiveData(light)

    HassTheme {
        Light(
            liveData,
            onToggle = { liveData.value = liveData.value!!.copy(state = it) },
            onBrightnessChange = {  }, // TODO: brightness vs brightness percent
            onColorTempChange = {}
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