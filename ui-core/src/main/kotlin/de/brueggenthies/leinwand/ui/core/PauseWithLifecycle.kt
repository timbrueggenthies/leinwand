package de.brueggenthies.leinwand.ui.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import de.brueggenthies.leinwand.core.MutableVideoPlayerState
import de.brueggenthies.leinwand.core.PlaybackState

@Composable
public fun PauseWithLifecycle(
    player: MutableVideoPlayerState,
    pauseBelow: Lifecycle.State = Lifecycle.State.RESUMED,
    restart: Boolean = true
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val latestRestart by rememberUpdatedState(newValue = restart)
    val latestPauseBelow by rememberUpdatedState(newValue = pauseBelow)
    val latestIsReady by rememberUpdatedState(newValue = player.playbackState == PlaybackState.Ready)
    DisposableEffect(lifecycleOwner, player) {
        var wasPlaying = player.isPlaying
        val observer: LifecycleObserver = LifecycleEventObserver { _, event ->
            when {
                event == Lifecycle.Event.downFrom(latestPauseBelow) -> {
                    wasPlaying = player.isPlaying
                    player.isPlaying = false
                }
                latestRestart && latestIsReady && event == Lifecycle.Event.upTo(latestPauseBelow) -> {
                    player.isPlaying = wasPlaying
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}