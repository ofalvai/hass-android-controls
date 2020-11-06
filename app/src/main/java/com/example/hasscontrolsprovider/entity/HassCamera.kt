package com.example.hasscontrolsprovider.entity

import java.time.ZonedDateTime

data class HassCamera(
    override val entityId: String,
    override val availability: Availability,
    override val name: String,
    override val status: String,
    override val lastChanged: ZonedDateTime?
) : HassControl