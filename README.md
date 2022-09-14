# Compose Video Player (WIP)

[![](https://jitpack.io/v/timbrueggenthies/leinwand.svg)](https://jitpack.io/#timbrueggenthies/leinwand)

A UI Component Library that enables Video Playback and Control in your Compose UI. The components are build from the ground up with Compose, so the only usage of the view based system is the the SurfaceView to display the Video playback.


# Usage

## Add dependency
For now, the library is only published via Jitpack. Therefore, to use the library, you need to include the repository in your build.gradle file.
```kotlin
allProjects {
  repositories {
    maven(url = "https://jitpack.io")  
  }
}
```
After that, you can add the dependency to the library
```kotlin
dependencies {
  implementation("com.github.timbrueggenthies.leinwand:core:<Version>")     // VideoPlayerState, can be used with completely custom UI
  implementation("com.github.timbrueggenthies.leinwand:ui-core:<Version>")  // Basic UI components, like a surface to play video
  implementation("com.github.timbrueggenthies.leinwand:ui:<Version>")       // Default UI components for video controls
}
```
## Use Video Player
To use the UI components, you first need to create a `VideoPlayerState` which wraps the `Player` from the androidx.media library and provides many information, all backed the the Compose `State` interfaces.
```kotlin
setContent {
  val player = createOwnPlayer() // Cann be for example exoplayer filled with media items
  val playerState = rememberVideoPlayerState(player) { 
    // optional init block
  }
}
```
`rememberVideoPlayerState` creates the State object and binds the attributes to the `Player`s event system.

### Use default UI components
Compose Video Player comes with a limited set of default components that get you up and running fast. They are customizable in a small scope, but you are
also free to create your own custom components very easily.
You can use the `VideoControlsContainer` to get a good Scaffold for video player controls.

### Only playback Video
If you don't need any video controls, you can use the `VideoPlaybackSurface` or `VideoPlayback` component, which allows you to display the content of a `Player`.
