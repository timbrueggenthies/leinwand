package de.brueggenthies.compose.video.demo.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.scope.DestinationScope
import de.brueggenthies.compose.video.demo.R
import de.brueggenthies.leinwand.core.PauseWithLifecycle
import de.brueggenthies.leinwand.core.RepeatMode
import de.brueggenthies.leinwand.core.rememberVideoPlayerState
import de.brueggenthies.leinwand.ui.ExperimentalVideoPlayerUi
import de.brueggenthies.leinwand.ui.SampleVideoControls
import de.brueggenthies.leinwand.ui.core.ResizeMode
import de.brueggenthies.leinwand.ui.core.VideoPlayback
import de.brueggenthies.leinwand.ui.core.aspectRatio

@Destination
@Composable
fun DestinationScope<Unit>.VideoControlsDemo() {
    val context = LocalContext.current
    val videoUrl1 = stringResource(id = R.string.stock_video_16_9)
    val videoUrl2 = stringResource(id = R.string.stock_video_9_16)
    val player = remember {
        val player = ExoPlayer.Builder(context).build()
        val firstItem = MediaItem.fromUri(videoUrl1)
        val secondItem = MediaItem.fromUri(videoUrl2)
        player.setMediaItem(firstItem)
        player.addMediaItem(secondItem)
        player.prepare()
        player
    }

    VideoDemo(player = player)
}

@OptIn(ExperimentalVideoPlayerUi::class)
@Composable
private fun VideoDemo(player: Player) {
    val playerState = rememberVideoPlayerState(player = player) {
        repeatMode = RepeatMode.All
        prepare()
    }
    PauseWithLifecycle(playerState, restart = false)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .systemBarsPadding()
                .padding(16.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            VideoPlayback(
                playerState = playerState,
                resizeMode = ResizeMode.Zoom,
                modifier = Modifier
                    .aspectRatio(playerState)
            )
            SampleVideoControls(
                videoPlayerState = playerState,
                modifier = Modifier
                    .matchParentSize()
            )
        }
    }
}