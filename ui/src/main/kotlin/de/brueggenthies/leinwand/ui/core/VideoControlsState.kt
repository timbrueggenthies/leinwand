package de.brueggenthies.leinwand.ui.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
public fun rememberVideoControlsState(init: VideoControlsState.() -> Unit = { }): VideoControlsState {
    val coroutineScope = rememberCoroutineScope()
    val videoControlsState = remember { VideoControlsStateImpl(coroutineScope) }
    init(videoControlsState)
    return videoControlsState
}

@Stable
public interface VideoControlsState {
    public var controlsVisible: Boolean
    public var hideAutomatically: Boolean
    public var hideCondition: HideCondition

    /**
     * This method allows other components to temporarily lock the automatic hiding of the controls and keeps them visible.
     * As long as the suspending call is not cancelled, the lock will stay.
     * The countdown for automatic hiding will start again if no lock is on the controls anymore.
     */
    public suspend fun keepVisible()
}

public fun interface HideCondition {
    public suspend fun waitToHide()
}

public class DurationCondition(private val duration: Duration) : HideCondition {
    override suspend fun waitToHide() {
        delay(duration)
    }
}

internal class VideoControlsStateImpl(coroutineScope: CoroutineScope) : VideoControlsState {

    init {
        startAutoHiding(coroutineScope)
    }

    override var hideAutomatically: Boolean by mutableStateOf(true)

    private var lockCount by mutableStateOf(0)

    override var hideCondition: HideCondition = DurationCondition(5.seconds)

    override var controlsVisible: Boolean by mutableStateOf(true)

    override suspend fun keepVisible() {
        lockCount++
        try {
            awaitCancellation()
        } finally {
            lockCount--
        }
    }

    private fun startAutoHiding(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            snapshotFlow { controlsVisible && hideAutomatically && lockCount == 0 }
                .collectLatest { startHiding ->
                    if (startHiding) {
                        hideCondition.waitToHide()
                        controlsVisible = false
                    }
                }
        }
    }
}
