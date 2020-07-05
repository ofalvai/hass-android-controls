package com.example.hasscontrolsprovider.network.request.websocket

import com.example.hasscontrolsprovider.network.WebSocketRequestType
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class AuthRequest(
    val access_token: String,
    val type: WebSocketRequestType = WebSocketRequestType.auth
)