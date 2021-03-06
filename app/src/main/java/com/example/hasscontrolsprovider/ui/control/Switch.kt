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
import com.example.hasscontrolsprovider.entity.Availability
import com.example.hasscontrolsprovider.entity.HassSwitch
import com.example.hasscontrolsprovider.ui.HassTheme
import java.time.ZonedDateTime

@Composable
fun SwitchControl(control: LiveData<HassSwitch>, onToggle: (Boolean) -> Unit) {
    val controlState by control.observeAsState(initialSwitchState)

    Control {
        EntityInfo(controlState) {
            Switch(
                checked = controlState.enabled,
                onCheckedChange = onToggle,
                color = MaterialTheme.colors.primary
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        EntityAttribute(name = "State", value = if (controlState.enabled) "ON" else "OFF")
    }
}

@Preview(name = "Switch")
@Composable
fun PreviewSwitchControl() {
    val switch = HassSwitch(
        entityId = "switch.zigbee2mqtt_permit_join",
        availability = Availability.AVAILABLE,
        name = "Zigbee2MQTT permit join",
        status = "on",
        lastChanged = ZonedDateTime.now().minusMinutes(8),
        enabled = false
    )
    val liveData = MutableLiveData(switch)

    HassTheme {
        SwitchControl(
            liveData,
            onToggle = { liveData.value = liveData.value!!.copy(enabled = it) }
        )
    }
}

private val initialSwitchState = HassSwitch(
    entityId = "",
    availability = Availability.UNAVAILABLE,
    name = "",
    status = "unavailable",
    lastChanged = null,
    enabled = false
)