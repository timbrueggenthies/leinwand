package de.brueggenthies.leinwand.ui.core

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

public enum class FullScreenMode {
    Off, On
}

@Composable
public fun ImmersiveMode(mode: FullScreenMode) {
    val context = LocalContext.current
    val controller = remember(context) {
        val window = context.findActivity()?.window ?: return@remember null
        WindowCompat.getInsetsController(window, window.decorView)
    } ?: return
    LaunchedEffect(controller) {
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
    DisposableEffect(controller, mode) {
        when (mode) {
            FullScreenMode.Off -> controller.show(WindowInsetsCompat.Type.systemBars())
            FullScreenMode.On -> controller.hide(WindowInsetsCompat.Type.systemBars())
        }
        onDispose {
            controller.show(WindowInsetsCompat.Type.systemBars())
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
