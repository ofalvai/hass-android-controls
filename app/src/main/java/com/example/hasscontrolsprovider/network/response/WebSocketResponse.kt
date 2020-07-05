package com.example.hasscontrolsprovider.network.response

import com.example.hasscontrolsprovider.network.WebSocketResponseType
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class WebSocketResponse(val type: WebSocketResponseType)