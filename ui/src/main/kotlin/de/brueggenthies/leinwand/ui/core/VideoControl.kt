package de.brueggenthies.leinwand.ui.core

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import de.brueggenthies.leinwand.ui.ExperimentalVideoPlayerUi
import de.brueggenthies.leinwand.core.MutableVideoPlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
@ExperimentalVideoPlayerUi
public fun VideoControlsContainer(
    playerState: MutableVideoPlayerState,
    modifier: Modifier = Modifier,
    controlsState: VideoControlsState = rememberVideoControlsState(),
    enterTransition: EnterTransition = fadeIn(),
    exitTransition: ExitTransition = fadeOut(),
    overlay: @Composable BoxScope.(MutableVideoPlayerState) -> Unit = { },
    content: @Composable VideoControlsComponentsScope.(MutableVideoPlayerState) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    VideoControlsContainer(
        modifier = modifier
            .clickable(interactionSource, null) { controlsState.controlsVisible = !controlsState.controlsVisible },
        playerState = playerState,
        visible = controlsState.controlsVisible,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        overlay = overlay,
        content = content
    )
}

@Composable
@ExperimentalVideoPlayerUi
private fun VideoControlsContainer(
    playerState: MutableVideoPlayerState,
    visible: Boolean,
    modifier: Modifier = Modifier,
    enterTransition: EnterTransition = fadeIn(),
    exitTransition: ExitTransition = fadeOut(),
    overlay: @Composable BoxScope.(MutableVideoPlayerState) -> Unit = { },
    content: @Composable VideoControlsComponentsScope.(MutableVideoPlayerState) -> Unit,
) {
    Box(modifier = modifier) {
        Box(modifier = Modifier.matchParentSize()) {
            overlay(playerState)
        }
        AnimatedVisibility(visible = visible, enter = enterTransition, exit = exitTransition, modifier = Modifier.matchParentSize()) {
            val innerScope = VideoControlsComponentsScopeImpl(this@Box, this)
            Box(modifier = Modifier.matchParentSize()) {
                content(innerScope, playerState)
            }
        }
    }
}

public interface VideoControlsComponentsScope : BoxScope, AnimatedVisibilityScope

public class VideoControlsComponentsScopeImpl(boxScope: BoxScope, animatedVisibilityScope: AnimatedVisibilityScope) :
    VideoControlsComponentsScope, BoxScope by boxScope,
    AnimatedVisibilityScope by animatedVisibilityScope
