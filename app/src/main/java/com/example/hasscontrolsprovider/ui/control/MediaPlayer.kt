package com.example.hasscontrolsprovider.ui.control

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.toStringAsFixed
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.ui.tooling.preview.Preview
import com.example.hasscontrolsprovider.R
import com.example.hasscontrolsprovider.entity.Availability
import com.example.hasscontrolsprovider.entity.HassMediaPlayer
import com.example.hasscontrolsprovider.ui.HassTheme
import com.example.hasscontrolsprovider.ui.secondaryTextColor
import java.time.ZonedDateTime

@Composable
fun MediaPlayerControl(
    control: LiveData<HassMediaPlayer>,
    onSkipPrevious: () -> Unit,
    onSkipNext: () -> Unit,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onVolumeChange: (Float) -> Unit
) {
    val controlState by control.observeAsState(initialMediaPlayerState)

    Control {
        EntityInfo(controlState) {
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = controlState.mediaTitle,
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = controlState.mediaArtist,
                    style = MaterialTheme.typography.subtitle1.copy(color = secondaryTextColor)
                )
            }
        }

        ControlButtons(
            modifier = Modifier.padding(top = 16.dp),
            controlState = controlState,
            onSkipPrevious = onSkipPrevious,
            onSkipNext = onSkipNext,
            onPlay = onPlay,
            onPause = onPause
        )

        LabeledSlider(
            label = "Volume",
            iconRes = R.drawable.ic_volume_full,
            value = controlState.volumeLevel,
            valueRange = 0..1,
            onValueChange = onVolumeChange
        )

        Spacer(modifier = Modifier.height(32.dp))
        EntityAttribute(name = "State", value = controlState.status)
        EntityAttribute(name = "Volume level", value = controlState.volumeLevel.toStringAsFixed(2))
    }
}

@Composable
private fun ControlButtons(
    modifier: Modifier = Modifier,
    controlState: HassMediaPlayer,
    onSkipPrevious: () -> Unit,
    onSkipNext: () -> Unit,
    onPlay: () -> Unit,
    onPause: () -> Unit
) {
    Row(modifier) {
        IconButton(onClick = onSkipPrevious) {
            Icon(
                asset = vectorResource(R.drawable.ic_skip_previous),
                tint = MaterialTheme.colors.onBackground
            )
        }

        val isPlaying = controlState.state == HassMediaPlayer.State.PLAYING
        IconButton(onClick = if (isPlaying) onPause else onPlay) {
            Icon(
                asset = vectorResource(
                    if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
                ),
                tint = MaterialTheme.colors.onBackground
            )
        }

        IconButton(onClick = onSkipNext) {
            Icon(
                asset = vectorResource(R.drawable.ic_skip_next),
                tint = MaterialTheme.colors.onBackground
            )
        }
    }
}

@Composable
@Preview(name = "Media Player")
fun PreviewMediaPlayerControl() {
    val mediaPlayer = HassMediaPlayer(
        entityId = "media_player.spotify",
        availability = Availability.AVAILABLE,
        name = "Spotify",
        status = "playing",
        lastChanged = ZonedDateTime.now().minusMinutes(1),
        state = HassMediaPlayer.State.PLAYING,
        volumeLevel = .75f,
        mediaTitle = "Dreams Like You",
        mediaArtist = "Yppah, Shaunna Heckman"
    )
    val liveData = MutableLiveData(mediaPlayer)

    HassTheme {
        MediaPlayerControl(
            liveData,
            onSkipPrevious = {
                liveData.value = liveData.value!!.copy(
                    mediaTitle = "Previous Title",
                    mediaArtist = "Previous Artist",
                    state = HassMediaPlayer.State.PLAYING,
                    status = "playing"
                )
            },
            onSkipNext = {
                liveData.value = liveData.value!!.copy(
                    mediaTitle = "Next Title",
                    mediaArtist = "Next Artist",
                    state = HassMediaPlayer.State.PLAYING,
                    status = "playing"
                )
            },
            onPlay = {
                liveData.value = liveData.value!!.copy(
                    state = HassMediaPlayer.State.PLAYING,
                    status = "playing"
                )
            },
            onPause = {
                liveData.value = liveData.value!!.copy(
                    state = HassMediaPlayer.State.PAUSED,
                    status = "paused"
                )
            },
            onVolumeChange = {
                liveData.value = liveData.value!!.copy(
                    // TODO: overrides initial value on first composition
                    volumeLevel = it
                )
            }
        )
    }
}

private val initialMediaPlayerState = HassMediaPlayer(
    entityId = "",
    availability = Availability.UNAVAILABLE,
    name = "",
    status = "unavailable",
    lastChanged = null,
    state = HassMediaPlayer.State.UNKNOWN,
    volumeLevel = 0f,
    mediaArtist = "",
    mediaTitle = ""
)