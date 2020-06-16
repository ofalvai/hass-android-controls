package com.example.hasscontrolsprovider.entity


class HassSwitch(
    override val entityId: String,
    override val isAvailable: Boolean,
    override val name: String,
    override val status: String,
    val enabled: Boolean
) : HassControl