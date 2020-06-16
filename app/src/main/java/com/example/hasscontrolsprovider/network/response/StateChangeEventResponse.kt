package com.example.hasscontrolsprovider.network.response

import com.example.hasscontrolsprovider.network.WebSocketEventType
import com.example.hasscontrolsprovider.network.WebSocketResponseType

class StateChangeEventResponse(
    val id: Int,
    val type: WebSocketResponseType,
    val event: Event
) {
    class Event(val data: EventData, val event_type: WebSocketEventType) {
        class EventData(
            val entity_id: String,
            val new_state: HassState,
            val old_state: HassState
        )
    }
}