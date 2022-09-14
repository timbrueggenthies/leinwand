package de.brueggenthies.leinwand.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import de.brueggenthies.leinwand.core.internal.ActualVideoPlayerState
import de.brueggenthies.leinwand.core.internal.PlayerListener
import de.brueggenthies.leinwand.core.internal.VideoPlayerStateHolder
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.isActive
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Stable
public interface VideoPlayerState {
    public val player: Player
    public val isPlaying: Boolean
    public val playWhenReady: Boolean
    public val isLoading: Boolean
    public val currentPosition: Duration
    public val currentPercentage: Float
    public val duration: Duration?
    public val bufferedPosition: Duration
    public val bufferedPercentage: Float
    public val repeatMode: RepeatMode
    public val videoSize: VideoSize?
    public val playbackState: PlaybackState
    public val mediaVolume: Float
    public val deviceVolume: Int
    public val deviceMuted: Boolean
    public val mediaMetadata: MediaMetadata
}

@Immutable
public class VideoSize(
    public val width: Int,
    public val height: Int,
    public val rotation: Int
) {
    public val intSize: IntSize = IntSize(width, height)
    public val aspectRatio: Float = width.toFloat() / height.toFloat()
}

@Stable
public interface MutableVideoPlayerState : VideoPlayerState {
    override var isPlaying: Boolean
    override var playWhenReady: Boolean
    override var repeatMode: RepeatMode
    override var deviceMuted: Boolean
    override var deviceVolume: Int
    override var mediaVolume: Float
    public fun prepare()
    public fun seekToPosition(position: Duration)
    public fun seekToPercentage(percentage: Float)
}

public enum class RepeatMode(@Player.RepeatMode internal val exoPlayerValue: Int) {
    Off(Player.REPEAT_MODE_OFF), One(Player.REPEAT_MODE_ONE), All(Player.REPEAT_MODE_ALL);

    internal companion object {
        fun ofExoplayerValue(@Player.RepeatMode value: Int): RepeatMode {
            return when (value) {
                Player.REPEAT_MODE_OFF -> Off
                Player.REPEAT_MODE_ONE -> One
                Player.REPEAT_MODE_ALL -> All
                else -> error("Unknown repeat mode: $value")
            }
        }
    }
}

public enum class PlaybackState {
    Idle,
    Ready,
    Buffering,
    Ended;

    internal companion object {
        fun ofExoplayerValue(@Player.State value: Int): PlaybackState {
            return when (value) {
                Player.STATE_IDLE -> Idle
                Player.STATE_READY -> Ready
                Player.STATE_BUFFERING -> Buffering
                Player.STATE_ENDED -> Ended
                else -> error("Unknown player state: $value")
            }
        }
    }
}

@Composable
public fun rememberVideoPlayerState(
    player: Player,
    init: MutableVideoPlayerState.() -> Unit = { }
): MutableVideoPlayerState {
    val (stateHolder, actualPlayerState) = remember(player) {
        val stateHolder = VideoPlayerStateHolder(player)
        val actualPlayerState = ActualVideoPlayerState(player, stateHolder)
        stateHolder to actualPlayerState
    }
    init(actualPlayerState)
    DisposableEffect(player) {
        val listener = PlayerListener(stateHolder)
        player.addListener(listener)
        onDispose { player.removeListener(listener) }
    }
    PollVideoTimeline(stateHolder, player)

    return actualPlayerState
}

@Composable
public fun rememberVideoPlayerState(
    init: ExoPlayer.Builder.() -> Unit = { }
): MutableVideoPlayerState {
    val context = LocalContext.current
    val player = remember(context) {
        ExoPlayer.Builder(context)
            .apply(init)
            .build()
    }
    DisposableEffect(player) {
        val currentPlayer = player
        onDispose { currentPlayer.release() }
    }
    return rememberVideoPlayerState(player = player)
}

@Composable
private fun PollVideoTimeline(
    stateHolder: VideoPlayerStateHolder,
    player: Player
) {
    LaunchedEffect(stateHolder) {
        while (isActive) {
            awaitFrame()
            stateHolder.currentPosition = player.currentPosition.milliseconds
            stateHolder.bufferedPosition = player.bufferedPosition.milliseconds
            stateHolder.bufferedPercentage = player.bufferedPercentage / 100f
        }
    }
}
