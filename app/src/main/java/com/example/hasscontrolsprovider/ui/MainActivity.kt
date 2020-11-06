package com.example.hasscontrolsprovider.ui

import android.os.Bundle
import android.text.format.DateUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.state
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.ui.tooling.preview.Preview
import com.example.hasscontrolsprovider.R
import com.example.hasscontrolsprovider.entity.*
import com.example.hasscontrolsprovider.mapper.PendingIntentConstants
import java.time.ZonedDateTime

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val hassControl = object : HassControl {
            override val entityId = intent.getStringExtra(PendingIntentConstants.EXTRA_ENTITY_ID) ?: ""
            override val availability = Availability.AVAILABLE
            override val name = intent.getStringExtra(PendingIntentConstants.EXTRA_NAME) ?: ""
            override val status = intent.getStringExtra(PendingIntentConstants.EXTRA_STATUS) ?: ""
            override val lastChanged: ZonedDateTime = ZonedDateTime.now() // TODO
        }

        val hassControlLiveData: LiveData<HassControl> = MutableLiveData(hassControl)

        setContent {
            HassTheme {
                Light(hassControlLiveData, onToggle = {})
            }
        }
    }
}

@Composable
fun Light(hassControl: LiveData<HassControl>, onToggle: (Boolean) -> Unit) {

    val controlState = hassControl.observeAsState()
    var entityState by state { false }
    var brightness by state { 0f }

    Column(modifier = Modifier.padding(16.dp)) {
        EntityInfo(hassControl) {
            Switch(
                checked = entityState,
                onCheckedChange = {
                    entityState = it
                    onToggle(it)
                }
            )
        }

        Text(
            text = "Brightness",
            style = MaterialTheme.typography.caption
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(vectorResource(R.drawable.ic_brightness))
            Slider(
                modifier = Modifier.padding(start = 16.dp),
                value = brightness,
                onValueChange = { brightness = it },
                valueRange = 0f..100f
            )
        }

        Text(
            text = "Color temperature",
            style = MaterialTheme.typography.caption
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(vectorResource(R.drawable.ic_color_temp))
            Slider(
                modifier = Modifier.padding(start = 16.dp),
                value = .0f,
                onValueChange = {  },
                valueRange = 0f..100f
            )
        }


        Divider(color = Color.Gray)
        EntityAttribute(name = "State", value = if (entityState) "ON" else "OFF")
        EntityAttribute(name = "Brightness", value = "${brightness.toInt()}%")
    }
}

@Composable
fun Switch(hassControl: LiveData<HassControl>, onToggle: (Boolean) -> Unit) {
    val controlState = hassControl.observeAsState()
    var entityState by state { false }

    Column(modifier = Modifier.padding(16.dp)) {
        EntityInfo(hassControl) {
            Switch(
                checked = entityState,
                onCheckedChange = {
                    entityState = it
                    onToggle(it)
                }
            )
        }

        Divider(color = Color.Gray)
        EntityAttribute(name = "State", value = if (entityState) "ON" else "OFF")
    }
}

@Composable
fun Sensor(hassControl: LiveData<HassControl>) {
    val controlState = hassControl.observeAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        EntityInfo(hassControl) {
            Text(controlState.value?.status ?: "")
        }
    }
}

@Composable
fun EntityInfo(hassControl: LiveData<HassControl>,
               currentStateContent: @Composable () -> Unit) {
    val controlState = hassControl.observeAsState()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(vectorResource(R.drawable.ic_lightbulb))
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = controlState.value?.name ?: "",
                    style = MaterialTheme.typography.subtitle1
                )
                val relativeLastChanged = controlState.value?.lastChanged?.toRelativeTimestamp()
                relativeLastChanged?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.subtitle1
                    )
                }
            }
        }
        currentStateContent()
    }
}

@Composable
fun EntityAttribute(name: String, value: String) {
    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = "$name: ", style = MaterialTheme.typography.body1)
        Text(text = value, style = MaterialTheme.typography.body1)
    }
}

@Preview(widthDp = 300, showBackground = true)
@Composable
fun PreviewLightEntity() {
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
    HassTheme {
        Light(MutableLiveData(light), onToggle = {})
    }
}

@Preview(widthDp = 300, showBackground = true)
@Composable
fun PreviewSwitchEntity() {
    val switch = HassSwitch(
        entityId = "switch.zigbee2mqtt_permit_join",
        availability = Availability.AVAILABLE,
        name = "Zigbee2MQTT permit join",
        status = "on",
        lastChanged = ZonedDateTime.now().minusMinutes(8),
        enabled = false
    )
    HassTheme {
        Switch(MutableLiveData(switch), onToggle = {})
    }
}

@Preview(widthDp = 300, showBackground = true)
@Composable
fun PreviewSensorEntity() {
    val sensor = object : HassControl {
        override val entityId = "sensor.temperature"
        override val availability =Availability.AVAILABLE
        override val name = "Temperature"
        override val status = "22.81 °C"
        override val lastChanged = ZonedDateTime.now().minusHours(1)

    }
    HassTheme {
        Sensor(MutableLiveData(sensor))
    }
}

@Composable
fun HassTheme(content: @Composable () -> Unit) {
    MaterialTheme(colors = colorPalette) {
        content()
    }
}

private fun ZonedDateTime.toRelativeTimestamp(): String {
    return DateUtils.getRelativeTimeSpanString(
        this.toEpochSecond() * 1000,
        System.currentTimeMillis(),
        DateUtils.SECOND_IN_MILLIS
    ).toString()
}