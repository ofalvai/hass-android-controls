package com.example.hasscontrolsprovider.entity


class HassSwitch(
    override val entityId: String,
    override val availability: Availability,
    override val name: String,
    override val status: String,
    val enabled: Boolean
) : HassControl