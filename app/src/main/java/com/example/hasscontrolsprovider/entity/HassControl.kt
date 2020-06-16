package com.example.hasscontrolsprovider.entity

interface HassControl {
    val entityId: String
    val isAvailable: Boolean
    val name: String
    val status: String
}