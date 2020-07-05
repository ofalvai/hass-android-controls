package com.example.hasscontrolsprovider.entity

data class HassCamera(
    override val entityId: String,
    override val availability: Availability,
    override val name: String,
    override val status: String
) : HassControl