package com.example.hasscontrolsprovider.entity

import java.time.ZonedDateTime

data class HassMediaPlayer(
    override val entityId: String,
    override val availability: Availability,
    override val name: String,
    override val status: String,
    override val lastChanged: ZonedDateTime?,

    val state: State,

    /**
     * Volume level in the range of 0..1
     */
    val volumeLevel: Float,

    val mediaTitle: String,
    val mediaArtist: String
) : HassControl {

    enum class State {
        PLAYING,
        PAUSED,
        IDLE,
        UNKNOWN
    }
}
