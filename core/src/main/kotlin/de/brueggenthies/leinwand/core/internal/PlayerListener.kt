package de.brueggenthies.leinwand.core.internal

import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import de.brueggenthies.leinwand.core.PlaybackState
import de.brueggenthies.leinwand.core.RepeatMode

internal class PlayerListener(private val videoPlayerState: VideoPlayerStateHolder) : Player.Listener {
    override fun onEvents(player: Player, events: Player.Events) {
        if (events.containsAny(Player.EVENT_TIMELINE_CHANGED, Player.EVENT_MEDIA_ITEM_TRANSITION)) {
            videoPlayerState.duration = player.duration.durationOrNull()
        }
    }

    override fun onVolumeChanged(volume: Float) {
        this.videoPlayerState.mediaVolume = volume
    }

    override fun onDeviceVolumeChanged(volume: Int, muted: Boolean) {
        this.videoPlayerState.deviceVolume = volume
        this.videoPlayerState.deviceMuted = muted
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        this.videoPlayerState.playbackState = PlaybackState.ofExoplayerValue(playbackState)
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
        videoPlayerState.repeatMode = RepeatMode.ofExoplayerValue(repeatMode)
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        videoPlayerState.isPlaying = isPlaying
    }

    override fun onVideoSizeChanged(videoSize: VideoSize) {
        videoPlayerState.videoSize = videoSize.asSize()
    }

    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
        videoPlayerState.mediaMetadata = mediaMetadata
    }

    override fun onIsLoadingChanged(isLoading: Boolean) {
        videoPlayerState.isLoading = isLoading
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        videoPlayerState.playWhenReady = playWhenReady
    }
}
