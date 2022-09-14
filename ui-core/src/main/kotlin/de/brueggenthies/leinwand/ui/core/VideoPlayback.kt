package de.brueggenthies.leinwand.ui.core

import android.view.SurfaceView
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import de.brueggenthies.leinwand.core.VideoPlayerState

@Composable
public fun VideoPlaybackSurface(player: Player, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val surfaceView: SurfaceView = remember { SurfaceView(context) }
    AndroidView(
        modifier = modifier
            .clipToBounds(),
        factory = { surfaceView }
    )
    DisposableEffect(player) {
        val currentPlayer = player
        currentPlayer.setVideoSurfaceView(surfaceView)
        onDispose {
            currentPlayer.clearVideoSurfaceView(surfaceView)
        }
    }
}

private fun Modifier.checkConstraints() = layout { measurable, constraints ->
    check(constraints.hasFixedHeight && constraints.hasFixedWidth) {
        """
            Missing size constraint.
            VideoPlayback and VideoPlaybackSurface have no intrinsic size, and therefore need strict incoming size constraints.
            You can achieve this by using:
            - height + width Modifier
            - fillMax[Size|Height|Width] Modifier
            
            If your component needs to resize based on the currently playing video, please use the aspectRatio(videoPlayerState) Modifier.
        """.trimIndent()
    }
    val placeable = measurable.measure(constraints)
    layout(placeable.width, placeable.height) { placeable.placeRelative(0, 0) }
}

@Composable
public fun VideoPlayback(
    playerState: VideoPlayerState,
    modifier: Modifier = Modifier,
    resizeMode: ResizeMode = ResizeMode.Fit,
    keepScreenOn: KeepScreenOnCondition = KeepScreenOnCondition.whilePlaying(playerState),
    fullScreenMode: FullScreenMode = FullScreenMode.Off
) {
    KeepScreenOnWhile(keepScreenOn)
    ImmersiveMode(fullScreenMode)
    Box(modifier = modifier) {
        VideoPlaybackSurface(
            player = playerState.player,
            modifier = Modifier
                .run {
                    playerState.videoSize?.let { resize(it.aspectRatio, resizeMode) } ?: this
                }
                .align(Alignment.Center)
        )
    }
}
