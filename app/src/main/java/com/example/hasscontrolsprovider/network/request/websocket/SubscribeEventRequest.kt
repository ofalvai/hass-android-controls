package com.example.hasscontrolsprovider.network.request.websocket

import com.example.hasscontrolsprovider.network.WebSocketEventType
import com.example.hasscontrolsprovider.network.WebSocketRequestType
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SubscribeEventRequest(
    val id: Int,
    val event_type: WebSocketEventType,
    val type: WebSocketRequestType = WebSocketRequestType.subscribe_events

)