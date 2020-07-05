package com.example.hasscontrolsprovider.network.response

import com.example.hasscontrolsprovider.network.WebSocketEventType
import com.example.hasscontrolsprovider.network.WebSocketResponseType
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class EventResponse(
    val id: Int,
    val type: WebSocketResponseType,
    val event: Event
) {
    @JsonClass(generateAdapter = true)
    class Event(val event_type: WebSocketEventType)
}