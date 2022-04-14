package de.brueggenthies.leinwand.core.internal

import androidx.media3.common.Player
import de.brueggenthies.leinwand.core.MutableVideoPlayerState
import de.brueggenthies.leinwand.core.RepeatMode
import de.brueggenthies.leinwand.core.VideoPlayerState
import kotlin.time.Duration

internal class ActualVideoPlayerState(
    override val player: Player,
    private val backingVideoPlayerState: VideoPlayerState
) : MutableVideoPlayerState {
    override val duration by backingVideoPlayerState::duration
    override val isLoading by backingVideoPlayerState::isLoading
    override val mediaMetadata by backingVideoPlayerState::mediaMetadata
    override val currentPosition by backingVideoPlayerState::currentPosition
    override val currentPercentage: Float by backingVideoPlayerState::currentPercentage
    override val bufferedPosition by backingVideoPlayerState::bufferedPosition
    override val bufferedPercentage by backingVideoPlayerState::bufferedPercentage
    override val videoSize by backingVideoPlayerState::videoSize
    override val playbackState by backingVideoPlayerState::playbackState

    override var deviceMuted: Boolean
        get() = backingVideoPlayerState.deviceMuted
        set(value) {
            player.isDeviceMuted = value
        }

    override var deviceVolume: Int
        get() = backingVideoPlayerState.deviceVolume
        set(value) {
            player.deviceVolume = value
        }

    override var mediaVolume: Float
        get() = backingVideoPlayerState.mediaVolume
        set(value) {
            player.volume = value
        }

    override var isPlaying: Boolean
        get() = backingVideoPlayerState.isPlaying
        set(value) {
            if (value) player.play() else player.pause()
        }

    override var playWhenReady: Boolean
        get() = backingVideoPlayerState.playWhenReady
        set(value) {
            player.playWhenReady = value
        }

    override var repeatMode: RepeatMode
        get() = backingVideoPlayerState.repeatMode
        set(value) {
            player.repeatMode = value.exoPlayerValue
        }

    override fun prepare() {
        player.prepare()
    }

    override fun seekToPosition(position: Duration) {
        if (!player.isCurrentMediaItemSeekable) return
        player.seekTo(position.inWholeMilliseconds)
    }

    override fun seekToPercentage(percentage: Float) {
        if (!player.isCurrentMediaItemSeekable) return
        val clamped = percentage.coerceIn(0f, 1f)
        val seekTo = duration?.let { (it.inWholeMilliseconds * clamped).toLong() } ?: 0L
        player.seekTo(seekTo)
    }
}