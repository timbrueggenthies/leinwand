package de.brueggenthies.leinwand.core.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import de.brueggenthies.leinwand.core.PlaybackState
import de.brueggenthies.leinwand.core.RepeatMode
import de.brueggenthies.leinwand.core.VideoPlayerState
import de.brueggenthies.leinwand.core.VideoSize
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

internal class VideoPlayerStateHolder(override val player: Player) : VideoPlayerState {
    override var isPlaying: Boolean by mutableStateOf(player.isPlaying)
    override var playWhenReady: Boolean by mutableStateOf(player.playWhenReady)
    override var isLoading: Boolean by mutableStateOf(player.isLoading)
    override var currentPosition: Duration by mutableStateOf(player.currentPosition.milliseconds)
    override val currentPercentage: Float
        get() = duration?.let { (currentPosition / it).toFloat() } ?: 0f
    override var duration: Duration? by mutableStateOf(player.duration.durationOrNull())
    override var bufferedPosition: Duration by mutableStateOf(player.bufferedPosition.milliseconds)
    override var bufferedPercentage: Float by mutableStateOf(player.bufferedPercentage / 100f)
    override var repeatMode: RepeatMode by mutableStateOf(RepeatMode.ofExoplayerValue(player.repeatMode))
    override var videoSize: VideoSize? by mutableStateOf(player.videoSize.asSize())
    override var playbackState: PlaybackState by mutableStateOf(PlaybackState.ofExoplayerValue(player.playbackState))
    override var deviceVolume: Int by mutableStateOf(player.deviceVolume)
    override var deviceMuted: Boolean by mutableStateOf(player.isDeviceMuted)
    override var mediaVolume: Float by mutableStateOf(player.volume)
    override var mediaMetadata: MediaMetadata by mutableStateOf(player.mediaMetadata)
}