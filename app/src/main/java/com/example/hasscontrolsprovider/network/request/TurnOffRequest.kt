package com.example.hasscontrolsprovider.network.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TurnOffRequest(
    val entity_id: String
)