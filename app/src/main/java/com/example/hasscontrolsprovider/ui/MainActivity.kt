package com.example.hasscontrolsprovider.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import androidx.ui.layout.Column
import androidx.ui.layout.padding
import androidx.ui.material.MaterialTheme
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.example.hasscontrolsprovider.entity.HassControl
import com.example.hasscontrolsprovider.mapper.PendingIntentConstants

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val hassControl = object : HassControl {
            override val entityId = intent.getStringExtra(PendingIntentConstants.EXTRA_ENTITY_ID)!!
            override val isAvailable = true
            override val name = intent.getStringExtra(PendingIntentConstants.EXTRA_NAME)!!
            override val status = intent.getStringExtra(PendingIntentConstants.EXTRA_STATUS)!!
        }

        setContent {
            MaterialTheme {
                Entity(hassControl)
            }
        }
    }
}

@Composable
fun Entity(hassControl: HassControl) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = hassControl.name, style = MaterialTheme.typography.h5)
        Text(text = hassControl.status, style = MaterialTheme.typography.body1)
    }
}

@Preview
@Composable
fun PreviewEntity() {
    val hassControl = object : HassControl {
        override val entityId = "light.living_room"
        override val isAvailable = true
        override val name = "Living room light"
        override val status = "OFF"
    }
    Entity(hassControl)
}