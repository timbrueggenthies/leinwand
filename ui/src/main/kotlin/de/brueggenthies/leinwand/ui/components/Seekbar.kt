package de.brueggenthies.leinwand.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitDragOrCancellation
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.constrain
import androidx.compose.ui.unit.dp
import de.brueggenthies.leinwand.core.MutableVideoPlayerState
import de.brueggenthies.leinwand.core.VideoPlayerState
import de.brueggenthies.leinwand.ui.ExperimentalVideoPlayerUi
import de.brueggenthies.leinwand.ui.lerp

@Stable
public class SeekbarState internal constructor() {
    public var state: SeekState by mutableStateOf(SeekState.Idle)
        internal set
}

public val SeekbarState.isSeeking: Boolean get() = state is SeekState.Seeking

public sealed class SeekState {
    public data class Seeking(val progress: Float) : SeekState()
    public object Idle : SeekState()
}

@Composable
@ExperimentalVideoPlayerUi
public fun rememberSeekbarState(): SeekbarState = remember { SeekbarState() }

@Composable
@ExperimentalVideoPlayerUi
public fun Timeline(videoPlayerState: VideoPlayerState, modifier: Modifier = Modifier) {
    Timeline(
        bufferedPercentage = videoPlayerState.bufferedPercentage,
        currentPercentage = videoPlayerState.currentPercentage,
        modifier = modifier
    )
}

@Composable
private fun Timeline(
    bufferedPercentage: Float,
    currentPercentage: Float,
    modifier: Modifier = Modifier,
    trackColor: Color = Color.White.copy(alpha = 0.4f),
    bufferColor: Color = Color.White,
    progressColor: Color = Color.Red
) {
    Box(
        modifier = modifier
            .height(4.dp)
            .fillMaxWidth()
            .clip(CircleShape)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(trackColor)
        )
        val animatedBufferedPercentage by animateFloatAsState(targetValue = bufferedPercentage)
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth { animatedBufferedPercentage }
                .background(bufferColor)
        )
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth { currentPercentage }
                .background(progressColor)
        )
    }
}

@Composable
@ExperimentalVideoPlayerUi
public fun Seekbar(
    videoPlayerState: MutableVideoPlayerState,
    modifier: Modifier = Modifier,
    seekbarState: SeekbarState = rememberSeekbarState(),
    pauseWhileSeeking: Boolean = false,
    deferSeeking: Boolean = true,
    trackColor: Color = Color.White.copy(alpha = 0.4f),
    bufferColor: Color = Color.White,
    progressColor: Color = Color.Red,
    thumbSize: Dp = 16.dp,
    thumbColor: Color = Color.White
) {
    val currentPercentage = (seekbarState.state as? SeekState.Seeking)?.progress ?: videoPlayerState.currentPercentage
    Box(
        modifier = modifier
            .seekbarPointerInput(videoPlayerState, seekbarState, pauseWhileSeeking, deferSeeking)
            .height(thumbSize)
    ) {
        Timeline(
            bufferedPercentage = videoPlayerState.bufferedPercentage,
            currentPercentage = currentPercentage,
            trackColor = trackColor,
            bufferColor = bufferColor,
            progressColor = progressColor,
            modifier = Modifier
                .padding(horizontal = thumbSize / 2)
                .align(Alignment.Center)
        )
        Box(
            modifier = Modifier
                .size(thumbSize)
                .clip(CircleShape)
                .background(thumbColor)
                .align(lerp(Alignment.CenterStart, Alignment.CenterEnd, currentPercentage))
        )
    }
}

private fun Modifier.fillMaxWidth(fractionProvider: () -> Float): Modifier = layout { measurable, constraints ->
    val width = (constraints.maxWidth * fractionProvider()).toInt()
    val updatedConstraints = constraints.constrain(Constraints.fixedWidth(width))
    val placeable = measurable.measure(updatedConstraints)
    layout(placeable.width, placeable.height) {
        placeable.placeRelative(0, 0)
    }
}

/**
 * [pointerInput] modifier that can be used to create a seekbar like behaviour on any component.
 *
 * @param videoPlayerState Video player on which the seeking should be performed
 * @param seekbarState Seekbar state that is used to communicate the current state of the seeking
 * @param pauseWhileSeeking Determines if the player should be paused during an active seek
 * @param deferSeeking Determines if the progress changes should be dispatches to the player immediately or only after the seeking has ended
 */
@ExperimentalVideoPlayerUi
public fun Modifier.seekbarPointerInput(
    videoPlayerState: MutableVideoPlayerState,
    seekbarState: SeekbarState,
    pauseWhileSeeking: Boolean = false,
    deferSeeking: Boolean = true
): Modifier = pointerInput(videoPlayerState, videoPlayerState.duration, seekbarState, pauseWhileSeeking, deferSeeking) {
    handleScrollbarTouch(videoPlayerState, seekbarState, pauseWhileSeeking, deferSeeking)
}

private suspend fun PointerInputScope.handleScrollbarTouch(
    videoPlayerState: MutableVideoPlayerState,
    seekbarState: SeekbarState,
    pauseWhileSeeking: Boolean,
    deferSeeking: Boolean
) {
    forEachGesture {
        awaitPointerEventScope {
            val down = awaitFirstDown()
            down.consume()
            seekbarState.state = SeekState.Seeking(videoPlayerState.currentPercentage)
            var currentProgress = videoPlayerState.currentPercentage
            val wasPlaying = videoPlayerState.isPlaying
            if (pauseWhileSeeking && wasPlaying) {
                videoPlayerState.isPlaying = false
            }
            while (true) {
                val next = awaitDragOrCancellation(down.id)
                // We only care about the initial pointer and no other remaining pointer
                if (next == null || next.id != down.id) {
                    if (pauseWhileSeeking && wasPlaying) {
                        videoPlayerState.isPlaying = true
                    }
                    if (deferSeeking) {
                        videoPlayerState.seekToPercentage(currentProgress)
                    }
                    seekbarState.state = SeekState.Idle
                    return@awaitPointerEventScope
                } else {
                    currentProgress = next.position.x / size.width
                    seekbarState.state = SeekState.Seeking(currentProgress)
                    if (!deferSeeking) {
                        videoPlayerState.seekToPercentage(currentProgress)
                    }
                }
            }
        }
    }
}
