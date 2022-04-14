package de.brueggenthies.compose.video.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.ramcosta.composedestinations.DestinationsNavHost
import de.brueggenthies.compose.video.demo.ui.NavGraphs
import de.brueggenthies.compose.video.demo.ui.theme.ComposeVideoPlayerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ComposeVideoPlayerTheme {
                DestinationsNavHost(NavGraphs.root)
            }
        }
    }
}
