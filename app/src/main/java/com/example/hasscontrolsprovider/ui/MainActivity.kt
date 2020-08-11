package com.example.hasscontrolsprovider.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.getValue
import androidx.compose.setValue
import androidx.compose.state
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import androidx.ui.graphics.Color
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.padding
import androidx.ui.livedata.observeAsState
import androidx.ui.material.Divider
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Slider
import androidx.ui.material.Switch
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.example.hasscontrolsprovider.entity.Availability
import com.example.hasscontrolsprovider.entity.HassControl
import com.example.hasscontrolsprovider.mapper.PendingIntentConstants

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val hassControl = object : HassControl {
            override val entityId = intent.getStringExtra(PendingIntentConstants.EXTRA_ENTITY_ID) ?: ""
            override val availability = Availability.AVAILABLE
            override val name = intent.getStringExtra(PendingIntentConstants.EXTRA_NAME) ?: ""
            override val status = intent.getStringExtra(PendingIntentConstants.EXTRA_STATUS) ?: ""
        }

//        val switchView = android.widget.Switch(this)
//        setContentView(switchView)

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
        Text(text = controlState.value?.name ?: "", style = MaterialTheme.typography.h5)
        Divider(color = Color.Gray)
        Switch(
            color = primaryColor,
            checked = entityState,
            onCheckedChange = {
                entityState = it
                onToggle(it)
            },
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp)
        )
        Slider(
            value = brightness,
            onValueChange = { brightness = it },
            valueRange = 0f..100f
        )
        Divider(color = Color.Gray)
        EntityAttribute(name = "State", value = if (entityState) "ON" else "OFF")
        EntityAttribute(name = "Brightness", value = "${brightness.toInt()}%")
    }
}

@Composable
fun EntityAttribute(name: String, value: String) {
    Row {
        Text(text = "$name: ", style = MaterialTheme.typography.body1)
        Text(text = value, style = MaterialTheme.typography.body1)
    }
}

@Preview(widthDp = 200, showBackground = true)
@Composable
fun PreviewEntity() {
    val hassControl = object : HassControl {
        override val entityId = "light.living_room"
        override val availability = Availability.AVAILABLE
        override val name = "Living room light"
        override val status = "OFF"
    }
    HassTheme {
        Light(MutableLiveData(hassControl), onToggle = {})
    }
}

@Composable
fun HassTheme(content: @Composable() () -> Unit) {
    MaterialTheme(colors = colorPalette) {
        content()
    }
}