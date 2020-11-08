package com.example.hasscontrolsprovider.ui.control

import android.text.format.DateUtils
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.hasscontrolsprovider.R
import com.example.hasscontrolsprovider.entity.HassControl
import com.example.hasscontrolsprovider.entity.HassLight
import com.example.hasscontrolsprovider.entity.HassSwitch
import com.example.hasscontrolsprovider.ui.iconDefaultColor
import com.example.hasscontrolsprovider.ui.secondaryTextColor
import java.time.ZonedDateTime

@Composable
fun Control(
    content: @Composable () -> Unit
) {
    Surface {
        Column(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}

@Composable
fun EntityAttribute(name: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "$name: ", style = MaterialTheme.typography.body2)
        Text(text = value, style = MaterialTheme.typography.body2)
    }
}

@Composable
fun EntityInfo(
    hassControl: HassControl,
    iconTint: Color = iconDefaultColor,
    currentStateContent: @Composable () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                asset = vectorResource(hassControl.toIconRes()),
                tint = iconTint
            )
            Column(modifier = Modifier.padding(start = 32.dp)) {
                Text(
                    text = hassControl.name,
                    style = MaterialTheme.typography.subtitle1
                )

                val relativeLastChanged = hassControl.lastChanged?.toRelativeTimestamp()
                relativeLastChanged?.let {
                    val style = MaterialTheme.typography.subtitle1.copy(color = secondaryTextColor)
                    Text(
                        text = it,
                        style = style
                    )
                }
            }
        }

        currentStateContent()
    }
}

@DrawableRes
private fun HassControl.toIconRes(): Int {
    return when (this) {
        is HassLight -> R.drawable.ic_lightbulb
        is HassSwitch -> R.drawable.ic_switch
        else -> R.drawable.ic_default_entity_icon
    }
}

private fun ZonedDateTime.toRelativeTimestamp(): String {
    return DateUtils.getRelativeTimeSpanString(
        this.toEpochSecond() * 1000,
        System.currentTimeMillis(),
        DateUtils.SECOND_IN_MILLIS
    ).toString()
}