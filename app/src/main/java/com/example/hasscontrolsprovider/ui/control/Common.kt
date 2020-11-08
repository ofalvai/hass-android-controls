package com.example.hasscontrolsprovider.ui.control

import android.text.format.DateUtils
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.hasscontrolsprovider.R
import com.example.hasscontrolsprovider.entity.Availability
import com.example.hasscontrolsprovider.entity.HassControl
import com.example.hasscontrolsprovider.entity.HassLight
import com.example.hasscontrolsprovider.entity.HassSwitch
import java.time.ZonedDateTime

@Composable
fun EntityAttribute(name: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "$name: ", style = MaterialTheme.typography.body1)
        Text(text = value, style = MaterialTheme.typography.body1)
    }
}

@Composable
fun EntityInfo(
    hassControl: HassControl,
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
            Icon(vectorResource(hassControl.toIconRes()))
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = hassControl.name,
                    style = MaterialTheme.typography.subtitle1
                )
                val relativeLastChanged = hassControl.lastChanged?.toRelativeTimestamp()
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

@DrawableRes
private fun HassControl.toIconRes(): Int {
    return when (this) {
        is HassLight -> R.drawable.ic_lightbulb
        is HassSwitch -> R.drawable.ic_switch
        else -> R.drawable.ic_default_entity_icon
    }
}

object InitialHassControl : HassControl {
    override val entityId = ""
    override val availability = Availability.UNAVAILABLE
    override val name = ""
    override val status = "unavailable"
    override val lastChanged = ZonedDateTime.now()
}

private fun ZonedDateTime.toRelativeTimestamp(): String {
    return DateUtils.getRelativeTimeSpanString(
        this.toEpochSecond() * 1000,
        System.currentTimeMillis(),
        DateUtils.SECOND_IN_MILLIS
    ).toString()
}