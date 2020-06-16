@file:Suppress("EnumEntryName")

package com.example.hasscontrolsprovider.network

enum class WebSocketRequestType {
    auth,
    subscribe_events
}

enum class WebSocketResponseType {
    auth_required,
    auth_invalid,
    auth_ok,
    result,
    event
}

enum class WebSocketEventType {
    state_changed
}