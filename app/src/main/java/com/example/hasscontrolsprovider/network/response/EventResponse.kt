package com.example.hasscontrolsprovider.network.response

import com.example.hasscontrolsprovider.network.WebSocketEventType
import com.example.hasscontrolsprovider.network.WebSocketResponseType

class EventResponse(
    val id: Int,
    val type: WebSocketResponseType,
    val event: Event
) {
    class Event(val event_type: WebSocketEventType)
}