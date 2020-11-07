package com.example.hasscontrolsprovider.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.state
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.ui.tooling.preview.Preview
import com.example.hasscontrolsprovider.entity.Availability
import com.example.hasscontrolsprovider.entity.HassControl
import com.example.hasscontrolsprovider.entity.HassSwitch
import com.example.hasscontrolsprovider.mapper.PendingIntentConstants
import com.example.hasscontrolsprovider.ui.control.EntityAttribute
import com.example.hasscontrolsprovider.ui.control.EntityInfo
import com.example.hasscontrolsprovider.ui.control.InitialHassControl
import java.time.ZonedDateTime

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val hassControl = object : HassControl {
            override val entityId =
                intent.getStringExtra(PendingIntentConstants.EXTRA_ENTITY_ID) ?: ""
            override val availability = Availability.AVAILABLE
            override val name = intent.getStringExtra(PendingIntentConstants.EXTRA_NAME) ?: ""
            override val status = intent.getStringExtra(PendingIntentConstants.EXTRA_STATUS) ?: ""
            override val lastChanged: ZonedDateTime = ZonedDateTime.now() // TODO
        }

        val hassControlLiveData: LiveData<HassControl> = MutableLiveData(hassControl)

        setContent {
            HassTheme {
                //Light(hassControlLiveData, onToggle = {})
            }
        }
    }
}



@Composable
fun Switch(hassControl: LiveData<HassControl>, onToggle: (Boolean) -> Unit) {
    val controlState by hassControl.observeAsState(InitialHassControl)
    var entityState by state { false }

    Column(modifier = Modifier.padding(16.dp)) {
        EntityInfo(controlState) {
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
    val controlState by hassControl.observeAsState(InitialHassControl)

    Column(modifier = Modifier.padding(16.dp)) {
        EntityInfo(controlState) {
            Text(controlState.status)
        }
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
        override val availability = Availability.AVAILABLE
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