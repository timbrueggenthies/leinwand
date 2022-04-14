package de.brueggenthies.leinwand.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.brueggenthies.leinwand.core.MutableVideoPlayerState
import de.brueggenthies.leinwand.core.PlaybackState
import de.brueggenthies.leinwand.ui.ExperimentalVideoPlayerUi
import de.brueggenthies.leinwand.ui.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
@ExperimentalVideoPlayerUi
public fun PlayPauseButton(
    videoPlayerState: MutableVideoPlayerState,
    modifier: Modifier = Modifier,
    playIcon: Int = R.drawable.ic_baseline_play_circle_outline_24,
    pauseIcon: Int = R.drawable.ic_baseline_pause_circle_outline_24,
    tint: Color = Color.White,
    enabled: Boolean = videoPlayerState.playbackState != PlaybackState.Idle
) {
    val showPlaying = !videoPlayerState.isPlaying
    val rotation by animateFloatAsState(targetValue = if (showPlaying) 0f else 90f)
    AnimatedContent(
        modifier = modifier
            .graphicsLayer { rotationZ = rotation }
            .clickable(
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false),
                onClick = { videoPlayerState.isPlaying = !videoPlayerState.isPlaying }
            ),
        transitionSpec = { fadeIn() with fadeOut() },
        targetState = showPlaying
    ) { showPlay ->
        Image(
            painter = painterResource(id = if (showPlay) playIcon else pauseIcon),
            colorFilter = ColorFilter.tint(tint),
            contentDescription = null,
            modifier = modifier
                .size(48.dp)
                .rotate(if (showPlay) 0f else -90f)
        )
    }
}