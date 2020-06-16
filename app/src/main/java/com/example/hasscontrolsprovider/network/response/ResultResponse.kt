package com.example.hasscontrolsprovider.network.response

import com.example.hasscontrolsprovider.network.WebSocketResponseType

class ResultResponse(
    val id: Int,
    val type: WebSocketResponseType,
    val success: Boolean
)

class ErrorResultResponse(
    val id: Int,
    val type: WebSocketResponseType,
    val success: Boolean,
    val error: Error
) {
    class Error(val code: Int, val message: String)
}