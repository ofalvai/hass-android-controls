package com.example.hasscontrolsprovider.entity

import java.time.ZonedDateTime


data class HassSwitch(
    override val entityId: String,
    override val availability: Availability,
    override val name: String,
    override val status: String,
    override val lastChanged: ZonedDateTime?,
    val enabled: Boolean
) : HassControl