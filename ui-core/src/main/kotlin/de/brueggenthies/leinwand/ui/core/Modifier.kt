package de.brueggenthies.leinwand.ui.core

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.ui.Modifier
import de.brueggenthies.leinwand.core.VideoPlayerState

public fun Modifier.aspectRatio(
    videoPlayerState: VideoPlayerState,
    defaultAspectRatio: Float = 16f / 9f,
    maxAspectRatio: Float = 21f / 9f,
    minAspectRatio: Float = 1f / 3f
): Modifier {
    val aspectRatio = videoPlayerState.videoSize?.run { aspectRatio } ?: defaultAspectRatio
    return aspectRatio(aspectRatio.coerceIn(minAspectRatio, maxAspectRatio))
}
