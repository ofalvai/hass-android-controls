package com.example.hasscontrolsprovider.network.response

import com.example.hasscontrolsprovider.network.WebSocketEventType
import com.example.hasscontrolsprovider.network.WebSocketResponseType
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class StateChangeEventResponse(
    val id: Int,
    val type: WebSocketResponseType,
    val event: Event
) {
    @JsonClass(generateAdapter = true)
    class Event(val data: EventData, val event_type: WebSocketEventType) {
        @JsonClass(generateAdapter = true)
        class EventData(
            val entity_id: String,
            val new_state: HassState,
            val old_state: HassState
        )
    }
}