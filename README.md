# Compose Video Player (WIP)

A UI Component Library that enables Video Playback and Control in your Compose UI. The components are build from the ground up with Compose, so the only usage of the view based system is the the SurfaceView to display the Video playback.


# Usage

## Add dependency
For now, the library is only published via Jitpack. Therefore, to use the library, you need to include the repository in your build.gradle file.
```kotlin
allProjects {
  repositories {  
    ...
	  maven { url 'https://jitpack.io' }  
	}
}
```
After that, you can add the dependency to the library
```kotlin
dependencies {
  ...
  implementation("lalala")
}
```
## Use Video Player
To use the UI components, you first need to create a `VideoPlayerState` which wraps the `Player` from the androidx.media library and provides many information, all backed the the Compose `State` interfaces.
```kotlin
setContent {
  val player = createPlayer() // Cann be for example exoplayer filled with media items
  val playerState = rememberVideoPlayerState(player)
  // Use playerState further on
}
```
`rememberVideoPlayerState` creates the State object and binds the attributes to the `Player`s event system. In addition, it also uses the current lifecycle to automatically pause the Video playback when the lifecycle reaches the `PAUSED` state.
If you need to use a custom lifecycle, you can provide a `LifecycleOwner`
```kotlin
rememberVideoPlayerState(player, lifecycleOwner = myCustomLifecycleOwener())
```

### Use default UI components
Compose Video Player comes with a complete set of default components that get you up and running fast.

### Only playback Video
If you don't need any video controls, you can use the `VideoPlaybackSurface` component, which allows you to display the content of a `Player`.
