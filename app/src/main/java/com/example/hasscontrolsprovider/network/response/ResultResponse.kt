package com.example.hasscontrolsprovider.network.response

import com.example.hasscontrolsprovider.network.WebSocketResponseType
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ResultResponse(
    val id: Int,
    val type: WebSocketResponseType,
    val success: Boolean
)

@JsonClass(generateAdapter = true)
class ErrorResultResponse(
    val id: Int,
    val type: WebSocketResponseType,
    val success: Boolean,
    val error: Error
) {
    @JsonClass(generateAdapter = true)
    class Error(val code: Int, val message: String)
}