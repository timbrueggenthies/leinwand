// ktlint-disable filename
@file:OptIn(ExperimentalVideoPlayerUi::class)

package de.brueggenthies.leinwand.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.referentialEqualityPolicy
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import de.brueggenthies.leinwand.core.MutableVideoPlayerState
import de.brueggenthies.leinwand.core.PlaybackState
import de.brueggenthies.leinwand.ui.components.PlayPauseButton
import de.brueggenthies.leinwand.ui.components.Seekbar
import de.brueggenthies.leinwand.ui.core.VideoControlsContainer

@Composable
private fun DefaultVideoControls(
    playbackState: MutableVideoPlayerState,
    modifier: Modifier = Modifier,
    editor: VideoControlsEditorScope.() -> Unit = { }
) {
    val editorScope =
        remember { VideoControlsEditorScope(defaultSeekBar, defaultPlayPauseButton, defaultBackground, defaultLoadingIndicator) }
    editorScope.editor()
    VideoControlsContainer(
        playerState = playbackState,
        modifier = modifier,
        overlay = { playerState ->
            if (playerState.playbackState == PlaybackState.Buffering) {
                Box(modifier = Modifier.align(Alignment.Center)) {
                    editorScope.loadingIndicator(playbackState)
                }
            }
        },
        content = { playerState ->
            editorScope.background(this, playbackState)
            if (playerState.playbackState != PlaybackState.Buffering) {
                Box(modifier = Modifier.align(Alignment.Center)) {
                    editorScope.playPauseButton(this@VideoControlsContainer, playbackState)
                }
            }
            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                editorScope.seekbar(this@VideoControlsContainer, playbackState)
            }
        }
    )
}

private val defaultBackground: @Composable AnimatedVisibilityScope.(MutableVideoPlayerState) -> Unit =
    {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )
    }

private val defaultPlayPauseButton: @Composable AnimatedVisibilityScope.(MutableVideoPlayerState) -> Unit =
    {
        PlayPauseButton(videoPlayerState = it)
    }

private val defaultSeekBar: @Composable AnimatedVisibilityScope.(MutableVideoPlayerState) -> Unit = {
    Seekbar(videoPlayerState = it)
}

private val defaultLoadingIndicator: @Composable (MutableVideoPlayerState) -> Unit = {
    CircularProgressIndicator(color = Color.White)
}

public class VideoControlsEditorScope internal constructor(
    seekbar: @Composable AnimatedVisibilityScope.(MutableVideoPlayerState) -> Unit,
    playPauseButton: @Composable AnimatedVisibilityScope.(MutableVideoPlayerState) -> Unit,
    background: @Composable AnimatedVisibilityScope.(MutableVideoPlayerState) -> Unit,
    loadingIndicator: @Composable (MutableVideoPlayerState) -> Unit
) {
    public var seekbar: @Composable AnimatedVisibilityScope.(MutableVideoPlayerState) -> Unit by mutableStateOf(
        seekbar,
        policy = referentialEqualityPolicy()
    )
    public var playPauseButton: @Composable AnimatedVisibilityScope.(MutableVideoPlayerState) -> Unit by mutableStateOf(
        playPauseButton,
        policy = referentialEqualityPolicy()
    )
    public var background: @Composable AnimatedVisibilityScope.(MutableVideoPlayerState) -> Unit by mutableStateOf(
        background,
        policy = referentialEqualityPolicy()
    )
    public var loadingIndicator: @Composable (MutableVideoPlayerState) -> Unit by mutableStateOf(
        loadingIndicator,
        policy = referentialEqualityPolicy()
    )
}
