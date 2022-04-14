package de.brueggenthies.leinwand.ui.core

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.getWindowInsetsController
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import de.brueggenthies.leinwand.core.VideoPlayerState

public fun interface KeepScreenOnCondition {
    public fun keepOn(): Boolean

    public companion object {
        public fun whileVisible(): KeepScreenOnCondition = KeepScreenOnCondition { true }
        public fun whilePlaying(playerState: VideoPlayerState): KeepScreenOnCondition = KeepScreenOnCondition { playerState.isPlaying }
        public fun never(): KeepScreenOnCondition = KeepScreenOnCondition { false }
    }
}

@Composable
public fun KeepScreenOnWhile(keepScreenOn: KeepScreenOnCondition) {
    val keep = keepScreenOn.keepOn()
    val context = LocalContext.current
    val window = remember(context) { context.findActivity()?.window }
    DisposableEffect(window, keep) {
        if (keep) {
            window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}

private fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}
