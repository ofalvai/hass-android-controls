package com.example.hasscontrolsprovider.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HassTheme {
                Column {
                    Button(
                        modifier = Modifier.padding(32.dp).fillMaxWidth(),
                        onClick = { startActivity(createShowkaseBrowserIntent(this@MainActivity)) }
                    ) {
                        Text("Launch Showkase")
                    }
                }
            }
        }
    }
}