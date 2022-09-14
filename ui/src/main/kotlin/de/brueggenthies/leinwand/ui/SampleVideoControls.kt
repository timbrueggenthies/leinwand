@file:OptIn(ExperimentalVideoPlayerUi::class)

package de.brueggenthies.leinwand.ui

import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.brueggenthies.leinwand.core.MutableVideoPlayerState
import de.brueggenthies.leinwand.core.PlaybackState
import de.brueggenthies.leinwand.core.VideoPlayerState
import de.brueggenthies.leinwand.ui.components.PlayPauseButton
import de.brueggenthies.leinwand.ui.components.Seekbar
import de.brueggenthies.leinwand.ui.components.isSeeking
import de.brueggenthies.leinwand.ui.components.rememberSeekbarState
import de.brueggenthies.leinwand.ui.core.VideoControlsComponentsScope
import de.brueggenthies.leinwand.ui.core.VideoControlsContainer
import de.brueggenthies.leinwand.ui.core.VideoControlsState
import de.brueggenthies.leinwand.ui.core.rememberVideoControlsState

@Composable
@ExperimentalVideoPlayerUi
public fun SampleVideoControls(
    videoPlayerState: MutableVideoPlayerState,
    modifier: Modifier = Modifier
) {
    val controlsState = rememberVideoControlsState {
        hideAutomatically = videoPlayerState.isPlaying
    }
    VideoControlsContainer(
        playerState = videoPlayerState,
        modifier = modifier,
        controlsState = controlsState,
        overlay = { playerState ->
            LoadingIndicator(playerState = playerState)
        },
        content = { playerState ->
            Background()
            PlayPauseButton(playerState = playerState)
            SeekBar(playerState = playerState, controlsState = controlsState)
        }
    )
}

@Composable
private fun BoxScope.Background() {
    Box(
        modifier = Modifier
            .matchParentSize()
            .background(Color.Black.copy(alpha = 0.3f))
    )
}

@Composable
private fun VideoControlsComponentsScope.PlayPauseButton(playerState: MutableVideoPlayerState) {
    PlayPauseButton(
        videoPlayerState = playerState,
        modifier = Modifier
            .align(Alignment.Center)
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun VideoControlsComponentsScope.SeekBar(playerState: MutableVideoPlayerState, controlsState: VideoControlsState) {
    val offset by transition.animateDp(label = "Offset of SeekBar") { enterExitState ->
        when (enterExitState) {
            EnterExitState.PreEnter -> 32.dp
            EnterExitState.Visible -> 0.dp
            EnterExitState.PostExit -> 32.dp
        }
    }
    val seekbarState = rememberSeekbarState()
    Seekbar(
        videoPlayerState = playerState,
        seekbarState = seekbarState,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .offset(y = offset)
    )
    LaunchedEffect(seekbarState.isSeeking) {
        if (seekbarState.isSeeking) {
            controlsState.keepVisible()
        }
    }
}

@Composable
private fun BoxScope.LoadingIndicator(playerState: VideoPlayerState) {
    if (playerState.playbackState == PlaybackState.Buffering) {
        CircularProgressIndicator(
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
