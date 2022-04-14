package de.brueggenthies.compose.video.demo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.RadioButton
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import de.brueggenthies.leinwand.ui.core.FullScreenMode
import de.brueggenthies.leinwand.ui.core.ResizeMode
import de.brueggenthies.leinwand.ui.core.VideoPlayback

@Destination
@Composable
fun DestinationScope<Unit>.VideoPlaybackDemo() {
    val context = LocalContext.current
    val videoUrl = stringResource(id = R.string.stock_video_16_9)
    val player = remember {
        val player = ExoPlayer.Builder(context).build()
        player.setMediaItem(MediaItem.fromUri(videoUrl))
        player
    }

    VideoDemo(player = player)
}

@Composable
private fun VideoDemo(player: Player) {
    val playerState = rememberVideoPlayerState(player = player) {
        playWhenReady = true
        repeatMode = RepeatMode.One
        prepare()
    }
    PauseWithLifecycle(playerState, restart = true)
    var fullscreenMode: FullScreenMode by remember { mutableStateOf(FullScreenMode.Off) }
    var resizeMode: ResizeMode by remember { mutableStateOf(ResizeMode.Fit) }
    Box(
        modifier = Modifier
            .systemBarsPadding()
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Column {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = fullscreenMode == FullScreenMode.On,
                    onCheckedChange = { checked -> fullscreenMode = if (checked) FullScreenMode.On else FullScreenMode.Off }
                )
                Text(text = "Toggle fullscreen mode")
            }
            ResizeModeRadioButton(resizeMode, ResizeMode.Fit) { resizeMode = it }
            ResizeModeRadioButton(resizeMode, ResizeMode.Fill) { resizeMode = it }
            ResizeModeRadioButton(resizeMode, ResizeMode.Zoom) { resizeMode = it }
            ResizeModeRadioButton(resizeMode, ResizeMode.FillHeight) { resizeMode = it }
            ResizeModeRadioButton(resizeMode, ResizeMode.FillWidth) { resizeMode = it }
            VideoPlayback(
                playerState = playerState,
                resizeMode = resizeMode,
                fullScreenMode = fullscreenMode,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            )
        }
    }
}

@Composable
fun ResizeModeRadioButton(current: ResizeMode, use: ResizeMode, choose: (ResizeMode) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val text = when (use) {
            ResizeMode.Fit -> "Fit"
            ResizeMode.FillWidth -> "FillWidth"
            ResizeMode.FillHeight -> "FillHeight"
            ResizeMode.Fill -> "Fill"
            ResizeMode.Zoom -> "Zoom"
        }
        RadioButton(selected = current == use, onClick = { choose(use) })
        Text(text)
    }
}
