package com.example.hasscontrolsprovider.network.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TurnOnRequest(
    val entity_id: String
)