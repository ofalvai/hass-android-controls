package com.example.hasscontrolsprovider.network.request.websocket

import com.example.hasscontrolsprovider.network.WebSocketRequestType

class AuthRequest(
    val access_token: String,
    val type: WebSocketRequestType = WebSocketRequestType.auth
)