package de.brueggenthies.compose.video.demo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.scope.DestinationScope
import de.brueggenthies.compose.video.demo.ui.destinations.VideoControlsDemoDestination
import de.brueggenthies.compose.video.demo.ui.destinations.VideoPlaybackDemoDestination

@RootNavGraph(start = true)
@Destination
@Composable
fun DestinationScope<Unit>.Home() {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { destinationsNavigator.navigate(VideoControlsDemoDestination) }) {
            Text(text = "Controls Demo")
        }
        Button(onClick = { destinationsNavigator.navigate(VideoPlaybackDemoDestination) }) {
            Text(text = "Playback Demo")
        }
    }
}

